# Tabla con todos los insumos y su último costo registrado
CREATE VIEW ultimoCostoInsumo AS
SELECT ac.idArticuloInsumo, ac.costo
FROM actualizacionCosto ac
         JOIN (
    SELECT idArticuloInsumo, MAX(fechaActualizacion) AS fechaReciente
    FROM actualizacionCosto
    GROUP BY idArticuloInsumo
) ult
ON ac.idArticuloInsumo = ult.idArticuloInsumo
AND ac.fechaActualizacion = ult.fechaReciente;