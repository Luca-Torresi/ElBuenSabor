# Se da de baja a los artículos manufacturados que tienen determinado insumo
DROP PROCEDURE IF EXISTS consecuenciasBajaInsumo;

DELIMITER $$
CREATE PROCEDURE consecuenciasBajaInsumo(IN _idInsumo INT)
BEGIN
    DECLARE _idArticulo INT;
    DECLARE _fechaBaja DATETIME;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT articulo.idArticulo, fechaBaja
        FROM articuloManufacturadoDetalle amd
        INNER JOIN articulo ON articulo.idArticulo = amd.idArticulo
        WHERE idArticuloInsumo = _idInsumo;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    OPEN nuevoCursor;
        bucle: LOOP
            FETCH nuevoCursor INTO _idArticulo, _fechaBaja;
            IF finCursor = 1 THEN
                LEAVE bucle;
            END IF;

            IF _fechaBaja IS NULL THEN
                UPDATE articulo SET fechaBaja = NOW() WHERE idArticulo = _idArticulo;
            END IF;

        END LOOP;
    CLOSE nuevoCursor;
END $$