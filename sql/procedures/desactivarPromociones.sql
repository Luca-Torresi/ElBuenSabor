# Desactiva las promociones que contienen un artículo en particular
DROP PROCEDURE IF EXISTS desactivarPromociones;

DELIMITER $$
CREATE PROCEDURE desactivarPromociones(IN _idArticulo INT)
BEGIN
    DECLARE _idPromocion INT;
    DECLARE _activo BOOLEAN;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT DISTINCT idPromocion, activo
        FROM promocion
        INNER JOIN detallePromocion ON detallePromocion.idPromocion = promocion.idPromocion
        WHERE idArticulo = _idArticulo;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    OPEN nuevoCursor;
        bucle: LOOP
            FETCH nuevoCursor INTO _idPromocion;
            IF finCursor = 1 THEN
                LEAVE bucle;
            END IF;

            IF _activo THEN
                UPDATE promocion SET activo = FALSE WHERE idPromocion = _idPromocion;
            END IF;

        END LOOP;
    CLOSE nuevoCursor;
END $$