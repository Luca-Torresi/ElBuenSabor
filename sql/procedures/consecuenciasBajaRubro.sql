# Se da de baja a los insumos pertenecientes a determinado rubro así como también a sus rubros hijos
DROP PROCEDURE IF EXISTS consecuenciasBajaRubro;

DELIMITER $$
CREATE PROCEDURE consecuenciasBajaRubro(IN _idRubro INT)
BEGIN
    DECLARE _idInsumo INT;
    DECLARE _fechaBaja DATE;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE cursor1 CURSOR FOR
        SELECT idArticuloInsumo, fechaBaja
        FROM articuloInsumo
        WHERE idRubroInsumo = _idRubro;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    OPEN cursor1;
        bucle1: LOOP
            FETCH cursor1 INTO _idInsumo, _fechaBaja;
            IF finCursor = 1 THEN
                LEAVE bucle1;
            END IF;

            IF _fechaBaja IS NULL THEN
                UPDATE articuloInsumo SET fechaBaja = NOW() WHERE idArticuloInsumo = _idInsumo;
            END IF;

        END LOOP;
    CLOSE cursor1;
END $$










