# Verifica si hay stock suficiente para elaborar al menos un artículo manufacturado
DELIMITER $$
CREATE FUNCTION sePuedeElaborar(_idArticulo INT)
    RETURNS BOOLEAN
    DETERMINISTIC
    READS SQL DATA
BEGIN
    DECLARE _insuficientes INT DEFAULT 0;

    SELECT COUNT(*) INTO _insuficientes
    FROM articuloManufacturadoDetalle amd
             INNER JOIN articuloInsumo ai ON ai.idArticuloInsumo = amd.idArticuloInsumo
    WHERE idArticulo = _idArticulo AND stockActual < cantidad;

    RETURN (_insuficientes = 0);
END $$