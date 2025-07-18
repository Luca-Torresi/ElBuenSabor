# Devuelve el margen de ganancia de la categoría del producto recibido como parámetro
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