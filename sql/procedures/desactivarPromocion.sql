# Desactiva la promoción correspondiente con el artículo que se dio de baja (si es que existe)
DELIMITER $$
CREATE PROCEDURE desactivarPromocion(IN _idArticulo INT)
BEGIN
    UPDATE promocion
    SET activo = FALSE
    WHERE idArticulo = _idArticulo;
END $$