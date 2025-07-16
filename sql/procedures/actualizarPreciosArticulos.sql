# Actualiza el precio de venta de todos los artículos

DROP PROCEDURE IF EXISTS actualizarPreciosArticulos;

DELIMITER $$
CREATE PROCEDURE actualizarPreciosArticulos()
BEGIN
    DECLARE _idArticulo INT;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idArticulo
        FROM articulo;

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