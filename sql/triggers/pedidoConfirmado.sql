# Cuando se confirma un nuevo pedido, se descuentan los insumos utilizados en su prerparación
DROP TRIGGER IF EXISTS pedidoConfirmado_au;

DELIMITER $$
CREATE TRIGGER pedidoConfirmado_au
    AFTER UPDATE ON pedido
    FOR EACH ROW
BEGIN
    IF NEW.estadoPedido = 'EN_PREPARACION' THEN
        CALL recorrerDetallesPedido(NEW.idPedido, TRUE);
    END IF;
END $$