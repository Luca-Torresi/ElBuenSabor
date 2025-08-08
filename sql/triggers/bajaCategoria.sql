# Cuando se da de baja una categoría, se debe dar de baja a las categorías hijas así como también a los artículos pertenecientes a esta categoría
DROP TRIGGER IF EXISTS bajaCategoria;

DELIMITER $$
CREATE TRIGGER bajaCategoria
    AFTER UPDATE ON categoria
    FOR EACH ROW
BEGIN
    IF OLD.fechaBaja IS NULL AND NEW.fechaBaja IS NOT NULL THEN
        CALL consecuenciasBajaCategoria(NEW.idCategoria);
    END IF;
END $$