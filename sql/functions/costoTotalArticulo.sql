# Devuelve el costo total de un artículo en base a sus ingredientes
DELIMITER $$
CREATE FUNCTION costoTotalArticulo(_idArticulo INT)
    RETURNS DECIMAL(10,2)
    DETERMINISTIC

BEGIN
    DECLARE _costoTotal DECIMAL(10,2);

    SELECT SUM(cantidad*costo) INTO _costoTotal
    FROM articuloManufacturadoDetalle amd
    INNER JOIN ultimoCostoInsumo uci ON uci.idArticuloInsumo = amd.idArticuloInsumo
    WHERE idArticulo = _idArticulo;

    RETURN _costoTotal;
END $$