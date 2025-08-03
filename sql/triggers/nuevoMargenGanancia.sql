/*
# Al modificar el margen de ganancia de una categoría, se actualizan los precios de venta de todos los artículos correspondientes
DROP TRIGGER IF EXISTS nuevoMargenGanancia_au;

DELIMITER $$
CREATE TRIGGER nuevoMargenGanancia_au
    AFTER UPDATE ON categoria
    FOR EACH ROW
BEGIN
    IF OLD.margenGanancia != NEW.margenGanancia THEN
        CALL actualizarPreciosPorCategoria(NEW.idCategoria);
    END IF;
END $$
*/