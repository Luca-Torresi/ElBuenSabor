# Para cada detalle del pedido recién confirmado, se descuentan los insumos requeridos para su preparación
DROP PROCEDURE IF EXISTS recorrerDetallesPedido;

DELIMITER $$
CREATE PROCEDURE recorrerDetallesPedido(IN _idPedido INT, IN _pedidoConfirmado BOOLEAN)
BEGIN
    DECLARE _idArticulo INT;
    DECLARE _idPromocion INT;
    DECLARE _cantidad INT;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idArticulo, idPromocion, cantidad
        FROM detallePedido
        WHERE idPedido = _idPedido;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    OPEN nuevoCursor;
        bucle: LOOP
            FETCH nuevoCursor INTO _idArticulo, _idPromocion, _cantidad;
            IF finCursor THEN
                LEAVE bucle;
            END IF;

            IF _idArticulo IS NOT NULL THEN
                CALL actualizarStock(_idArticulo, _cantidad, _pedidoConfirmado);
            ELSEIF _idPromocion IS NOT NULL THEN
                CALL recorrerDetallesPromocion(_idPromocion, _cantidad, _pedidoConfirmado);
            END IF;
        END LOOP;
    CLOSE nuevoCursor;

END $$