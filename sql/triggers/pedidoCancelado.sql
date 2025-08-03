# Si el cliente o el cajero cancelan el pedido, se revierten los cambios realizados en el stock
DROP TRIGGER IF EXISTS pedidoCancelado_au;

DELIMITER $$
CREATE TRIGGER pedidoCancelado_au
    AFTER UPDATE ON pedido
    FOR EACH ROW
BEGIN
    IF NEW.estadoPedido = 'CANCELADO' OR NEW.estadoPedido = 'RECHAZADO' THEN
        CALL recorrerDetallesPedido(NEW.idPedido, FALSE);
    END IF;
END $$
