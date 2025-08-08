# Cuando se da de baja a un rubro, se debe dar de baja a los rubros hijos así como también a los insumos asociados a este
DROP TRIGGER IF EXISTS bajaRubro;

DELIMITER $$
CREATE TRIGGER bajaRubro
    AFTER UPDATE ON rubroInsumo
    FOR EACH ROW
BEGIN
    IF OLD.fechaBaja IS NULL AND NEW.fechaBaja IS NOT NULL THEN
        CALL consecuenciasBajaRubro(NEW.idRubroInsumo);
    END IF;
END $$
