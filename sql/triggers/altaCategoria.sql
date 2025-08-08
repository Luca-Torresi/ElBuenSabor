/*
# Cuando una categoría padre se da de alta, se evalua si las categorías hijas se deben dar de alta también
DROP TRIGGER IF EXISTS altaCategoria_au;

DELIMITER $$
CREATE TRIGGER altaCategoria_au
    AFTER UPDATE ON categoria
    FOR EACH ROW
BEGIN
    IF OLD.fechaBaja IS NOT NULL AND NEW.fechaBaja IS NULL THEN
        CALL consecuenciasAltaCategoria(NEW.idCategoria, OLD.fechaBaja);
    END IF;
END $$
*/