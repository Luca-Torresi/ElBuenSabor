# En la tabla temporal 'comparacionInsumos', actualiza el valor correspondiente de insumos necesarios para la promoción
DROP PROCEDURE IF EXISTS calcularInsumosRequeridos;

DELIMITER $$
CREATE PROCEDURE calcularInsumosRequeridos(IN _idArticulo INT, IN _cantArticulo INT)
BEGIN
    DECLARE _idInsumo INT;
    DECLARE _cantInsumo DECIMAL(6,3);
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idArticuloInsumo, cantidad
        FROM articuloManufacturadoDetalle
        WHERE idArticulo = _idArticulo;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    OPEN nuevoCursor;
    bucle: LOOP
        FETCH nuevoCursor INTO _idInsumo, _cantInsumo;
        IF finCursor THEN
            LEAVE bucle;
        END IF;

        UPDATE comparacionInsumos
        SET stockNecesario = stockNecesario + (_cantInsumo * _cantArticulo)
        WHERE idArticuloInsumo = _idInsumo;

    END LOOP;
    CLOSE nuevoCursor;
END $$