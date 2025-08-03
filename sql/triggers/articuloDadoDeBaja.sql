# Cuando un artículo es dado de baja, se deben desactivar todas las promociones que contengan dicho artículo
DROP TRIGGER IF EXISTS articuloDadoDeBaja_au;

DELIMITER $$
CREATE TRIGGER articuloDadoDeBaja_au
    AFTER UPDATE ON articulo
    FOR EACH ROW
BEGIN
    IF OLD.fechaBaja IS NULL AND NEW.fechaBaja IS NOT NULL THEN
       CALL desactivarPromociones(NEW.idArticulo);
    END IF;
END $$