# Verifica si hay stock suficiente para elaborar la promoción indicada
DROP PROCEDURE IF EXISTS sePuedeElaborarPromocion;

DELIMITER $$
CREATE PROCEDURE sePuedeElaborarPromocion(IN _idPromocion INT, OUT _puedeElaborarse BOOLEAN)
bloquePrincipal: BEGIN

    DECLARE _idArticulo INT;
    DECLARE _esManufacturado BOOLEAN;
    DECLARE _cantArticulo INT;
    DECLARE _stockNoElaborado INT;
    DECLARE _idInsumo INT;
    DECLARE _stockInsumo DECIMAL(6,3);
    DECLARE _stockNecesario DECIMAL(6,3);
    DECLARE finCursor INT DEFAULT 0;

    DECLARE cursor1 CURSOR FOR
        SELECT idArticulo, esManufacturado, cantidad
        FROM detallePromocion
        INNER JOIN articulo ON articulo.idArticulo = detallePromocion.idArticulo
        WHERE idPromocion = _idPromocion;

    DECLARE cursor2 CURSOR FOR
        SELECT * FROM comparacionInsumos;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    DROP TEMPORARY TABLE IF EXISTS comparacionInsumos;
    CREATE TEMPORARY TABLE comparacionInsumos(
        idArticuloInsumo INT,
        stockActual DECIMAL(6,3),
        stockNecesario DECIMAL(6,3) DEFAULT 0
    );
    INSERT INTO comparacionInsumos (idArticuloInsumo, stockActual)
    SELECT idArticuloInsumo, stockActual
    FROM articuloInsumo;

    OPEN cursor1;
        bucle1: LOOP
            FETCH cursor1 INTO _idArticulo, _esManufacturado, _cantArticulo;
            IF finCursor THEN
                LEAVE bucle1;
            END IF;

            IF _esManufacturado THEN
                CALL calcularInsumosRequeridos(_idArticulo, _cantArticulo);
            ELSE
                SELECT stock INTO _stockNoElaborado
                FROM articuloNoElaborado
                INNER JOIN articulo ON articulo.idArticulo = articuloNoElaborado.idArticulo
                WHERE articulo.idArticulo = _idArticulo;

                IF(_stockNoElaborado < _cantArticulo) THEN
                    DROP TEMPORARY TABLE IF EXISTS comparacionInsumos;
                    SET _puedeElaborarse = FALSE;
                    LEAVE bloquePrincipal;
                END IF;
            END IF;

        END LOOP;
    CLOSE cursor1;

    SET finCursor = 0;

    OPEN cursor2;
        bucle2: LOOP
            FETCH cursor2 INTO _idInsumo, _stockInsumo, _stockNecesario;
            IF finCursor THEN
                LEAVE bucle2;
            END IF;

            IF _stockInsumo < _stockNecesario THEN
                DROP TEMPORARY TABLE IF EXISTS comparacionInsumos;
                SET _puedeElaborarse = FALSE;
                LEAVE bloquePrincipal;
            END IF;
        END LOOP;
    CLOSE cursor2;

    SET _puedeElaborarse = TRUE;

    DROP TEMPORARY TABLE IF EXISTS comparacionInsumos;
END bloquePrincipal $$

