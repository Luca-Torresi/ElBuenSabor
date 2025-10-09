# Promedio de cantidad de pedidos por hora en el día
DROP VIEW IF EXISTS promedioPedidosPorHora;

CREATE VIEW promedioPedidosPorHora AS
SELECT
    HOUR(fechaYHora) AS hora_num_orden,
    CONCAT(HOUR(fechaYHora), 'hs') AS hora,
    ROUND(COUNT(*) / (SELECT COUNT(*) FROM pedido) * 100) AS porcentaje
FROM pedido
GROUP BY
    hora_num_orden,
    hora
ORDER BY
    hora_num_orden;
