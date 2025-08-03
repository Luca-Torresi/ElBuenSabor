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
    DECLARE _idPromocion INT;
    DECLARE _cantidad INT;
    DECLARE _precioUnitario DECIMAL(10,2);
    DECLARE _subtotal DECIMAL(10,2);
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idArticulo, idPromocion, cantidad, subtotal
        FROM detallePedido
        WHERE idPedido = _idPedido;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    SELECT metodoDePago, tipoEnvio, total
    INTO _metodoDePago,_tipoEnvio, _total
    FROM pedido
    WHERE idPedido = _idPedido;

    INSERT INTO factura(idPedido, nroComprobante, fechaYHora, metodoDePago, total)
    VALUES(_idPedido, generarNumeroComprobante(), NOW(), _metodoDePago, _total);

    SET _idFactura = LAST_INSERT_ID();

    OPEN nuevoCursor;
        bucle: LOOP
            FETCH nuevoCursor INTO _idArticulo, _idPromocion, _cantidad, _subtotal;
            IF finCursor THEN
                LEAVE bucle;
            END IF;

            SET _precioUnitario = _subtotal / _cantidad;

            INSERT INTO detalleFactura(idFactura, idArticulo, idPromocion, cantidad, precioUnitario, subtotal)
            VALUES(_idFactura, _idArticulo, _idPromocion, _cantidad, _precioUnitario, _subtotal);

        END LOOP;
    CLOSE nuevoCursor;
END $$
