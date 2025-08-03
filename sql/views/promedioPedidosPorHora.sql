# Promedio de cantidad de pedidos por hora en el día
DROP VIEW IF EXISTS promedioPedidosPorHora;

CREATE VIEW promedioPedidosPorHora AS
SELECT
    CONCAT(HOUR(fechaYHora), 'hs') AS hora,
    ROUND(COUNT(*) / (SELECT COUNT(*) FROM pedido) * 100) AS porcentaje
FROM pedido
GROUP BY HOUR(fechaYHora)
ORDER BY HOUR(fechaYHora);
