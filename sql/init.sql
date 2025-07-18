# ---------------------------- FUNCTIONS ----------------------------

# Devuelve el costo total de un artículo en base a sus ingredientes
DROP FUNCTION IF EXISTS costoTotalArticulo;

DELIMITER $$
CREATE FUNCTION costoTotalArticulo(_idArticulo INT)
    RETURNS DECIMAL(10,2)
    DETERMINISTIC

BEGIN
    DECLARE _costoTotal DECIMAL(10,2);

    SELECT SUM(cantidad*costo) INTO _costoTotal
    FROM articuloManufacturadoDetalle amd
             INNER JOIN ultimoCostoInsumo uci ON uci.idArticuloInsumo = amd.idArticuloInsumo
    WHERE idArticulo = _idArticulo;

    RETURN _costoTotal;
END $$

# Genera un nuevo número de comprobante para una factura
DROP FUNCTION IF EXISTS generarNumeroComprobante;

DELIMITER $$
CREATE FUNCTION generarNumeroComprobante()
    RETURNS VARCHAR(255)
    DETERMINISTIC
BEGIN
    DECLARE _ultimoNro INT DEFAULT 0;

    SELECT IFNULL(CAST(nroComprobante AS UNSIGNED), 0)
    INTO _ultimoNro
    FROM factura
    ORDER BY idFactura DESC
    LIMIT 1;

    RETURN LPAD(_ultimoNro + 1, 8, '0');
END $$

# Devuelve el margen de ganancia de la categoría del producto recibido como parámetro
DROP FUNCTION IF EXISTS margenArticulo;

DELIMITER $$
CREATE FUNCTION margenArticulo(_idArticulo INT)
    RETURNS DECIMAL(3,2)
    DETERMINISTIC

BEGIN
    DECLARE _margen DECIMAL(3,2);

    SELECT margenGanancia INTO _margen FROM categoria
                                                INNER JOIN articulo ON articulo.idCategoria = categoria.idCategoria
    WHERE idArticulo = _idArticulo;

    RETURN _margen;
END $$

# Verifica si hay stock suficiente para elaborar al menos un artículo manufacturado
DROP FUNCTION IF EXISTS sePuedeElaborar;

DELIMITER $$
CREATE FUNCTION sePuedeElaborar(_idArticulo INT)
    RETURNS BOOLEAN
    DETERMINISTIC
    READS SQL DATA
BEGIN
    DECLARE _insuficientes INT DEFAULT 0;

    SELECT COUNT(*) INTO _insuficientes
    FROM articuloManufacturadoDetalle amd
             INNER JOIN articuloInsumo ai ON ai.idArticuloInsumo = amd.idArticuloInsumo
    WHERE idArticulo = _idArticulo AND stockActual < cantidad;

    RETURN (_insuficientes = 0);
END $$


# ---------------------------- PROCEDURES ----------------------------

# Actualiza el precio de venta de todos los artículos
DROP PROCEDURE IF EXISTS actualizarPreciosArticulos;

DELIMITER $$
CREATE PROCEDURE actualizarPreciosArticulos()
BEGIN
    DECLARE _idArticulo INT;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idArticulo
        FROM articulo;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    OPEN nuevoCursor;
    bucle: LOOP
        FETCH nuevoCursor INTO _idArticulo;
        IF finCursor THEN
            LEAVE bucle;
        END IF;

        CALL modificarPrecioVenta(_idArticulo);

    END LOOP;
    CLOSE nuevoCursor;
END $$

# Según si el pedido fue confirmado o cancelado, se realizan los cambios correspondientes en el stock
DROP PROCEDURE IF EXISTS actualizarStock;

DELIMITER $$
CREATE PROCEDURE actualizarStock(IN _idArticulo INT, IN _cantArticulo INT, IN _pedidoCancelado BOOLEAN)
BEGIN
    DECLARE _esManufacturado BOOLEAN;
    DECLARE _idInsumo INT;
    DECLARE _cantInsumo DECIMAL(6,3);
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idArticuloInsumo, cantidad
        FROM ArticuloManufacturadoDetalle
        WHERE idArticulo = _idArticulo;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    SELECT esManufacturado INTO _esManufacturado
    FROM articulo
    WHERE idArticulo = _idArticulo;

    IF _esManufacturado THEN
        OPEN nuevoCursor;
        bucle: LOOP
            FETCH nuevoCursor INTO _idInsumo, _cantInsumo;
            IF finCursor THEN
                LEAVE bucle;
            END IF;

            IF NOT _pedidoCancelado THEN
                UPDATE ArticuloInsumo
                SET stockActual = (stockActual - _cantInsumo * _cantArticulo)
                WHERE idArticuloInsumo = _idInsumo;
            ELSE
                UPDATE ArticuloInsumo
                SET stockActual = (stockActual + _cantInsumo * _cantArticulo)
                WHERE idArticuloInsumo = _idInsumo;
            END IF;

        END LOOP;
        CLOSE nuevoCursor;

    ELSE
        IF NOT _pedidoCancelado THEN
            UPDATE ArticuloNoElaborado
            SET stock = (stock - _cantArticulo)
            WHERE idArticulo = _idArticulo;
        ELSE
            UPDATE ArticuloNoElaborado
            SET stock = (stock + _cantArticulo)
            WHERE idArticulo = _idArticulo;
        END IF;
    END IF;

END $$

# Obtiene los detalles del pedido cancelado, para luego deshacer los cambios realizados en el stock
DROP PROCEDURE IF EXISTS detallesPedidoCancelado;

DELIMITER $$
CREATE PROCEDURE detallesPedidoCancelado(IN _idPedido INT)
BEGIN
    DECLARE _idArticulo INT;
    DECLARE _cantArticulo INT;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idArticulo, cantidad
        FROM detallePedido
        WHERE idPedido = _idPedido;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    OPEN nuevoCursor;
    bucle: LOOP
        FETCH nuevoCursor INTO _idArticulo, _cantArticulo;
        IF finCursor THEN
            LEAVE bucle;
        END IF;

        CALL actualizarStock(_idArticulo, _cantArticulo, TRUE);

    END LOOP;
    CLOSE nuevoCursor;
END $$

# Genera los registros correspondientes en las tablas 'factura' y 'detalleFactura' asociados al pedido
DROP PROCEDURE IF EXISTS generarNuevaFactura;

DELIMITER $$
CREATE PROCEDURE generarNuevaFactura(IN _idPedido INT)
BEGIN
    DECLARE _idFactura INT;
    DECLARE _metodoDePago VARCHAR(255);
    DECLARE _tipoEnvio VARCHAR(255);
    DECLARE _total DECIMAL(10,2) DEFAULT 0;
    DECLARE _idArticulo INT;
    DECLARE _nombre VARCHAR(255);
    DECLARE _cantArticulo INT;
    DECLARE _precioVenta DECIMAL(10,2);
    DECLARE _subtotal DECIMAL(10,2);
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT dp.idArticulo, nombre, cantidad, precioVenta
        FROM detallePedido dp
                 INNER JOIN articulo ON articulo.idArticulo = dp.idArticulo
        WHERE idPedido = _idPedido;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    SELECT metodoDePago, tipoEnvio
    INTO _metodoDePago,_tipoEnvio
    FROM pedido
    WHERE idPedido = _idPedido;

    INSERT INTO factura(idPedido, nroComprobante, fechaYHora, metodoDePago)
    VALUES(_idPedido, generarNumeroComprobante(), NOW(), _metodoDePago);

    SET _idFactura = LAST_INSERT_ID();

    OPEN nuevoCursor;
    bucle: LOOP
        FETCH nuevoCursor INTO _idArticulo, _nombre, _cantArticulo, _precioVenta;
        IF finCursor THEN
            LEAVE bucle;
        END IF;

        SET _subtotal = _precioVenta * _cantArticulo;
        SET _total = _total + _subtotal;

        INSERT INTO detalleFactura(idFactura, idArticulo, nombreArticulo, cantidad, precioUnitario, subtotal)
        VALUES(_idFactura, _idArticulo, _nombre, _cantArticulo, _precioVenta, _subtotal);

    END LOOP;
    CLOSE nuevoCursor;

    IF _tipoEnvio = 'DELIVERY' THEN
        SET _total = _total + 1500;
    END IF;

    UPDATE factura
    SET total = _total
    WHERE idFactura = _idFactura;
END $$

# Actualiza el precio de venta de un artículo teniendo en cuenta su costo y el margen de ganancia de su categoría
DROP PROCEDURE IF EXISTS modificarPrecioVenta;

DELIMITER $$
CREATE PROCEDURE modificarPrecioVenta(IN _idArticulo INT)
BEGIN
    DECLARE _esManufacturado BOOLEAN;

    SELECT esManufacturado INTO _esManufacturado
    FROM articulo
    WHERE idArticulo = _idArticulo;

    IF _esManufacturado THEN
        UPDATE articulo
        SET precioVenta = ROUND(costoTotalArticulo(_idArticulo) * (1 + margenArticulo(_idArticulo)), -2)
        WHERE idArticulo = _idArticulo;
    ELSE
        UPDATE articulo
        SET precioVenta = ROUND((SELECT costo FROM articuloNoElaborado WHERE idArticulo = _idArticulo) * (1 + margenArticulo(_idArticulo)), -2)
        WHERE idArticulo = _idArticulo;
    END IF;
END $$


# ---------------------------- TRIGGERS ----------------------------

# Cuando se realiza un nuevo pedido, se descuentan los insumos utilizados en su prerparación
DROP TRIGGER IF EXISTS descontarInsumos_ai;

DELIMITER $$
CREATE TRIGGER descontarInsumos_ai
    AFTER INSERT ON detallePedido
    FOR EACH ROW
BEGIN
    CALL actualizarStock(NEW.idArticulo, NEW.cantidad, FALSE);
END $$

# Si el cliente o el cajero cancelan el pedido, se revierten los cambios realizados en el stock
DROP TRIGGER IF EXISTS deshacerCambiosStock_au;

DELIMITER $$
CREATE TRIGGER deshacerCambiosStock_au
    AFTER UPDATE ON pedido
    FOR EACH ROW
BEGIN
    IF NEW.estadoPedido = 'CANCELADO' OR NEW.estadoPedido = 'RECHAZADO' THEN
        CALL detallesPedidoCancelado(NEW.idPedido);
    END IF;
END $$

# Una vez que el pedido es entregado se guardan los datos correspondientes a la factura
DROP TRIGGER IF EXISTS pedidoEntregado_au;

DELIMITER $$
CREATE TRIGGER pedidoEntregado_au
    AFTER UPDATE ON pedido
    FOR EACH ROW
BEGIN
    IF NEW.estadoPedido = 'ENTREGADO' THEN
        CALL generarNuevaFactura(NEW.idPedido);
    END IF;
END $$


# ---------------------------- VIEWS ----------------------------

# Artículos más vendidos
DROP VIEW IF EXISTS articulosMasVendidos;

CREATE VIEW articulosMasVendidos AS
SELECT
    articulo.nombre AS nombreArticulo,
    categoria.nombre AS nombreCategoria,
    SUM(cantidad) AS cantVendido
FROM detallePedido
         INNER JOIN pedido ON pedido.idPedido = detallePedido.idPedido
         INNER JOIN articulo ON articulo.idArticulo = detallePedido.idArticulo
         INNER JOIN categoria ON categoria.idCategoria = articulo.idCategoria
WHERE estadoPedido = 'ENTREGADO'
GROUP BY nombreArticulo
ORDER BY cantVendido DESC;

# Cantidad de clientes repartida según el número de pedidos realizados
DROP VIEW IF EXISTS frecuenciaClientes;

CREATE VIEW frecuenciaClientes AS
WITH registroMes AS (
    SELECT
        DATE_FORMAT(u.registro, '%Y-%m') AS mes,
        u.idUsuario AS clienteId,
        u.registro
    FROM usuario u
             JOIN cliente c ON c.idUsuario = u.idUsuario
    WHERE u.idRol = 3
),
     pedidos30DesdeRegistro AS (
         SELECT
             r.mes,
             r.clienteId,
             COUNT(p.idPedido) AS pedidos30
         FROM registroMes r
                  LEFT JOIN pedido p
                            ON p.idCliente = r.clienteId
                                AND p.fechaYHora BETWEEN r.registro AND DATE_ADD(r.registro, INTERVAL 30 DAY)
         GROUP BY r.mes, r.clienteId
     ),
     clientesAgrupados AS (
         SELECT
             mes,
             SUM(CASE WHEN pedidos30 = 0 THEN 1 ELSE 0 END) AS clientesSinPedido
         FROM pedidos30DesdeRegistro
         GROUP BY mes
     ),
     primerPedido AS (
         -- Obtener primer pedido de cada cliente
         SELECT
             p.idCliente AS clienteId,
             MIN(p.fechaYHora) AS fechaPrimerPedido
         FROM pedido p
         GROUP BY p.idCliente
     ),
     clientesConUnPedidoMes AS (
         -- Clientes cuyo primer pedido fue en ese mes y no tuvieron segundo pedido en 30 días después
         SELECT
             DATE_FORMAT(pp.fechaPrimerPedido, '%Y-%m') AS mes,
             COUNT(*) AS clientesConUnPedido
         FROM primerPedido pp
                  LEFT JOIN pedido p2
                            ON p2.idCliente = pp.clienteId
                                AND p2.fechaYHora > pp.fechaPrimerPedido
                                AND p2.fechaYHora <= DATE_ADD(pp.fechaPrimerPedido, INTERVAL 30 DAY)
         WHERE p2.idPedido IS NULL -- no hay segundo pedido en 30 días
         GROUP BY mes
     ),
     clientesRecurrentes AS (
         -- Clientes que tienen segundo o más pedidos
         SELECT
             DATE_FORMAT(p.fechaYHora, '%Y-%m') AS mes,
             p.idCliente AS clienteId
         FROM pedido p
         WHERE (
                   SELECT COUNT(*)
                   FROM pedido p2
                   WHERE p2.idCliente = p.idCliente
                     AND p2.fechaYHora < p.fechaYHora
               ) >= 1
         GROUP BY mes, p.idCliente
     ),
     clientesRecurrentesAgrupados AS (
         SELECT
             mes,
             COUNT(*) AS clientesRecurrentes
         FROM clientesRecurrentes
         GROUP BY mes
     )

SELECT
    COALESCE(c.mes, u.mes, r.mes) AS mes,
    COALESCE(c.clientesSinPedido, 0) AS clientesSinPedido,
    COALESCE(u.clientesConUnPedido, 0) AS clientesConUnPedido,
    COALESCE(r.clientesRecurrentes, 0) AS clientesRecurrentes
FROM clientesAgrupados c
         LEFT JOIN clientesConUnPedidoMes u ON c.mes = u.mes
         LEFT JOIN clientesRecurrentesAgrupados r ON c.mes = r.mes

UNION

SELECT
    COALESCE(c.mes, u.mes, r.mes) AS mes,
    COALESCE(c.clientesSinPedido, 0) AS clientesSinPedido,
    COALESCE(u.clientesConUnPedido, 0) AS clientesConUnPedido,
    COALESCE(r.clientesRecurrentes, 0) AS clientesRecurrentes
FROM clientesAgrupados c
         RIGHT JOIN clientesConUnPedidoMes u ON c.mes = u.mes
         RIGHT JOIN clientesRecurrentesAgrupados r ON u.mes = r.mes

ORDER BY mes;

# Insumos cuyo stock actual se encuentre por debajo del stock establecido como minimo
DROP VIEW IF EXISTS insumosQueReponer;

CREATE VIEW insumosQueReponer AS
SELECT idArticuloInsumo, nombre, stockActual, stockMinimo
FROM articuloInsumo
WHERE stockActual <= stockMinimo;

# Promedio de cantidad de pedidos por hora en el día
DROP VIEW IF EXISTS promedioPedidosPorHora;

CREATE VIEW promedioPedidosPorHora AS
SELECT
    CONCAT(HOUR(fechaYHora), 'hs') AS hora,
    ROUND(COUNT(*) / (SELECT COUNT(*) FROM pedido) * 100) AS porcentaje
FROM pedido
GROUP BY HOUR(fechaYHora)
ORDER BY HOUR(fechaYHora);

# Número de ventas y total recaudado por mes
DROP VIEW IF EXISTS recaudadoPorMes;

CREATE VIEW recaudadoPorMes AS
SELECT
    DATE_FORMAT(fechaYHora, '%M %Y') AS mes,
    COUNT(*) AS cantVentas,
    SUM(total) AS totalRecaudado
FROM factura
GROUP BY YEAR(fechaYHora), MONTH(fechaYHora)
ORDER BY YEAR(fechaYHora) ASC, MONTH(fechaYHora) ASC;

# Tabla con todos los insumos y su último costo registrado
DROP VIEW IF EXISTS ultimoCostoInsumo;

CREATE VIEW ultimoCostoInsumo AS
SELECT ac.idArticuloInsumo, ac.costo
FROM actualizacionCosto ac
         JOIN (
    SELECT idArticuloInsumo, MAX(fechaActualizacion) AS fechaReciente
    FROM actualizacionCosto
    GROUP BY idArticuloInsumo
) ult
              ON ac.idArticuloInsumo = ult.idArticuloInsumo
                  AND ac.fechaActualizacion = ult.fechaReciente;