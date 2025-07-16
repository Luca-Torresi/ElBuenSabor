# Si el cliente o el cajero cancelan el pedido, se revierten los cambios realizados en el stock

DROP TRIGGER IF EXISTS deshacerCambiosStock_au;

DELIMITER $$
CREATE TRIGGER deshacerCambiosStock_au
    AFTER UPDATE ON pedido
    FOR EACH ROW
BEGIN
    IF NEW.estadoPedido = 'CANCELADO' OR NEW.estadoPedido = 'RECHAZADO' THEN
        CALL detallesPedidoCancelado(NEW.idPedido);
    END IF;
END $$
