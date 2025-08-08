# Cuando se da de baja un insumo, se deben dar de baja también los artículos que contienen dicho insumo
DROP TRIGGER IF EXISTS bajaInsumo_au;

DELIMITER $$
CREATE TRIGGER bajaInsumo_au
    AFTER UPDATE ON articuloInsumo
    FOR EACH ROW
BEGIN
    IF OLD.fechaBaja IS NULL AND NEW.fechaBaja IS NOT NULL THEN
        CALL consecuenciasBajaInsumo(NEW.idArticuloInsumo);
    END IF;
END $$
