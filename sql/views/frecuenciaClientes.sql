# Cantidad de clientes repartida según el número de pedidos realizados
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
