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