# Obtiene los artículos manufacturados que contienen un insumo determinado, para luego modificar el precio de venta
DROP PROCEDURE IF EXISTS articulosConDeterminadoInsumo;

DELIMITER $$
CREATE PROCEDURE articulosConDeterminadoInsumo(IN _idInsumo INT)
BEGIN
    DECLARE _idArticulo INT;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT am.idArticulo FROM articuloManufacturado am
        INNER JOIN articuloManufacturadoDetalle amd ON amd.idArticulo = am.idArticulo
        WHERE idArticuloInsumo = _idInsumo;

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