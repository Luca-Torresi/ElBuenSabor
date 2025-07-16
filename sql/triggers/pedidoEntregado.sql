# Una vez que el pedido es entregado se guardan los datos correspondientes a la factura

DROP TRIGGER IF EXISTS pedidoEntregado_au;

DELIMITER $$
CREATE TRIGGER pedidoEntregado_au
    AFTER UPDATE ON pedido
    FOR EACH ROW
BEGIN
    IF NEW.estadoPedido = 'ENTREGADO' THEN
        CALL generarNuevaFactura(NEW.idPedido);
    END IF;
END $$

