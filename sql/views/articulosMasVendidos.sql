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