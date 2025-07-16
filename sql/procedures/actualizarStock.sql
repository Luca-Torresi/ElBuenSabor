# Según si el pedido fue confirmado o cancelado, se realizan los cambios correspondientes en el stock

DROP PROCEDURE IF EXISTS actualizarStock;

DELIMITER $$
CREATE PROCEDURE actualizarStock(IN _idArticulo INT, IN _cantArticulo INT, IN _pedidoCancelado BOOLEAN)
BEGIN
    DECLARE _esManufacturado BOOLEAN;
    DECLARE _idInsumo INT;
    DECLARE _cantInsumo DECIMAL(6,3);
    DECLARE finCursor INT DEFAULT 0;

    DECLARE nuevoCursor CURSOR FOR
        SELECT idArticuloInsumo, cantidad
        FROM ArticuloManufacturadoDetalle
        WHERE idArticulo = _idArticulo;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finCursor = 1;

    SELECT esManufacturado INTO _esManufacturado
    FROM articulo
    WHERE idArticulo = _idArticulo;

    IF _esManufacturado THEN
        OPEN nuevoCursor;
        bucle: LOOP
            FETCH nuevoCursor INTO _idInsumo, _cantInsumo;
            IF finCursor THEN
                LEAVE bucle;
            END IF;

            IF NOT _pedidoCancelado THEN
                UPDATE ArticuloInsumo
                SET stockActual = (stockActual - _cantInsumo * _cantArticulo)
                WHERE idArticuloInsumo = _idInsumo;
            ELSE
                UPDATE ArticuloInsumo
                SET stockActual = (stockActual + _cantInsumo * _cantArticulo)
                WHERE idArticuloInsumo = _idInsumo;
            END IF;

        END LOOP;
        CLOSE nuevoCursor;

    ELSE
        IF NOT _pedidoCancelado THEN
            UPDATE ArticuloNoElaborado
            SET stock = (stock - _cantArticulo)
            WHERE idArticulo = _idArticulo;
        ELSE
            UPDATE ArticuloNoElaborado
            SET stock = (stock + _cantArticulo)
            WHERE idArticulo = _idArticulo;
        END IF;
    END IF;

END $$