# Cuando se realiza un nuevo pedido, se descuentan los insumos utilizados en su prerparación

DROP TRIGGER IF EXISTS descontarInsumos_ai;

DELIMITER $$
CREATE TRIGGER descontarInsumos_ai
    AFTER INSERT ON detallePedido
    FOR EACH ROW
BEGIN
    CALL actualizarStock(NEW.idArticulo, NEW.cantidad, FALSE);
END $$