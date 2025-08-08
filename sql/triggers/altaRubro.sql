/*
# Cuando un rubro padre se da de alta, se evalua si los rubros hijos se deben dar de alta también
DROP TRIGGER IF EXISTS altaRubro_au;

DELIMITER $$
CREATE TRIGGER altaRubro_au
    AFTER UPDATE ON rubroInsumo
    FOR EACH ROW
BEGIN
    IF OLD.fechaBaja IS NOT NULL AND NEW.fechaBaja IS NULL THEN
        CALL consecuenciasAltaRubro(NEW.idRubroInsumo, OLD.fechaBaja);
    END IF;
END $$
*/