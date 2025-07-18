# Obtiene los detalles del pedido cancelado, para luego deshacer los cambios realizados en el stock
DELIMITER $$
CREATE PROCEDURE detallesPedidoCancelado(IN _idPedido INT)
BEGIN
    DECLARE _idArticulo INT;
    DECLARE _cantArticulo INT;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idArticulo, cantidad
        FROM detallePedido
        WHERE idPedido = _idPedido;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    OPEN nuevoCursor;
    bucle: LOOP
        FETCH nuevoCursor INTO _idArticulo, _cantArticulo;
        IF finCursor THEN
            LEAVE bucle;
        END IF;

        CALL actualizarStock(_idArticulo, _cantArticulo, TRUE);

    END LOOP;
    CLOSE nuevoCursor;
END $$