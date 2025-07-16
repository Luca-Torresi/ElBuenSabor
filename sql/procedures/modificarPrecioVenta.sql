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