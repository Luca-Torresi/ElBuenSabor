# Se da de baja a los artículos pertenecientes a determinada categoría así como también a las categorías hijas
DROP PROCEDURE IF EXISTS consecuenciasBajaCategoria;

DELIMITER $$
CREATE PROCEDURE consecuenciasBajaCategoria(IN _idCategoria INT)
BEGIN
    DECLARE _idArticulo INT;
    DECLARE _fechaBaja DATE;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE cursor1 CURSOR FOR
        SELECT idArticulo, fechaBaja
        FROM articulo
        WHERE idCategoria = _idCategoria;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    OPEN cursor1;
        bucle1: LOOP
            FETCH cursor1 INTO _idArticulo, _fechaBaja;
            IF finCursor = 1 THEN
                LEAVE bucle1;
            END IF;

            IF _fechaBaja IS NULL THEN
                UPDATE articulo SET fechaBaja = NOW() WHERE idArticulo = _idArticulo;
            END IF;

        END LOOP;
    CLOSE cursor1;
END $$










