/*
# Da de alta a los rubros que fueron dados de baja junto con su rubro padre
DROP PROCEDURE IF EXISTS consecuenciasAltaRubro;

DELIMITER $$
CREATE PROCEDURE consecuenciasAltaRubro(IN _idRubro INT, IN _fechaBajaPadre DATE)
BEGIN
    DECLARE _idRubroHijo INT;
    DECLARE _fechaBajaHijo DATE;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idRubroInsumo, fechaBaja
        FROM rubroInsumo
        WHERE idRubroIsumoPadre = _idRubro;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    OPEN nuevoCursor;
        bucle: LOOP
            FETCH nuevoCursor INTO _idRubroHijo, _fechaBajaHijo;
            IF finCursor = 1 THEN
                LEAVE bucle;
            END IF;

            IF _fechaBajaHijo = _fechaBajaPadre THEN
                UPDATE rubroInsumo SET fechaBaja = NULL WHERE idRubroInsumo = _idRubroHijo;
            END IF;

        END LOOP;
    CLOSE nuevoCursor;
END $$
*/