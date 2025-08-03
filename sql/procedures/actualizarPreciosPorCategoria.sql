# Para cada artículo que pertenece a la categoría indicada, se actualiza el precio de venta con el nuevo margen de ganancia
DROP PROCEDURE IF EXISTS actualizarPreciosPorCategoria;

DELIMITER $$
CREATE PROCEDURE actualizarPreciosPorCategoria(IN _idCategoria INT)
BEGIN
    DECLARE _idArticulo INT;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idArticulo FROM articulo
        WHERE idCategoria = _idCategoria;

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