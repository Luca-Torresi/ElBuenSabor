/*
# Cada vez que se actualiza el costo de un artículo insumo, se modifica el precio de los artículos correspondientes
DROP TRIGGER IF EXISTS nuevoCostoInsumo_ai;

DELIMITER $$
CREATE TRIGGER nuevoCostoInsumo_ai
    AFTER INSERT ON actualizacionCosto
    FOR EACH ROW
BEGIN
    CALL articulosConDeterminadoInsumo(NEW.idArticuloInsumo);
END $$
*/