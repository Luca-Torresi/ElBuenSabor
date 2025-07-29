# Cuando se da de baja un artículo que está asociado a una promoción, la promoción también se debe dar de baja
DELIMITER $$
CREATE TRIGGER desactivarPromocion_au
    AFTER UPDATE ON articulo
    FOR EACH ROW
BEGIN
    IF OLD.fechaBaja IS NULL AND NEW.fechaBaja IS NOT NULL THEN
        CALL desactivarPromocion(OLD.idArticulo);
    END IF;
END $$