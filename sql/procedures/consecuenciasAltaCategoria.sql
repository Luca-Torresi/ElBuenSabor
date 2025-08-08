/*
# Da de alta a las categorías que fueron dadas de baja junto con su categoría padre
DROP PROCEDURE IF EXISTS consecuenciasAltaCategoria;

DELIMITER $$
CREATE PROCEDURE consecuenciasAltaCategoria(IN _idCategoria INT, IN _fechaBajaPadre DATE)
BEGIN
    DECLARE _idCategoriaHija INT;
    DECLARE _fechaBajaHija DATE;
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idCategoria, fechaBaja
        FROM categoria
        WHERE idCategoriaPadre = _idCategoria;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    OPEN nuevoCursor;
        bucle: LOOP
            FETCH nuevoCursor INTO _idCategoriaHija, _fechaBajaHija;
            IF finCursor = 1 THEN
                LEAVE bucle;
            END IF;

            IF _fechaBajaHija = _fechaBajaPadre THEN
                UPDATE categoria SET fechaBaja = NULL WHERE idCategoria = _idCategoriaHija;
            END IF;

        END LOOP;
    CLOSE nuevoCursor;
END $$
*/