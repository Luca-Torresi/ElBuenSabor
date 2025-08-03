# Realiza los cambios en el stock correspondientes a las promociones según si el pedido fue cofirmado o cancelado
DROP PROCEDURE IF EXISTS recorrerDetallesPromocion;

DELIMITER $$
CREATE PROCEDURE recorrerDetallesPromocion(IN _idPromocion INT, IN _cantidad INT, IN _pedidoConfirmado BOOLEAN)
BEGIN
    DECLARE _idArticulo INT;
    DECLARE _cantArticulo INT;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idArticulo, cantidad
        FROM detallePromocion
        WHERE idPromocion = _idPromocion;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    OPEN nuevoCursor;
        bucle: LOOP
            FETCH nuevoCursor INTO _idArticulo, _cantArticulo;
            IF finCursor THEN
                LEAVE bucle;
            END IF;

            CALL actualizarStock(_idArticulo, _cantArticulo * _cantidad, _pedidoConfirmado);

        END LOOP;
    CLOSE nuevoCursor;
END $$