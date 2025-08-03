# Insumos cuyo stock actual se encuentre por debajo del stock establecido como minimo
DROP VIEW IF EXISTS insumosQueReponer;

CREATE VIEW insumosQueReponer AS
SELECT idArticuloInsumo, nombre, stockActual, stockMinimo
FROM articuloInsumo
WHERE stockActual <= stockMinimo;