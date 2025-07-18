# Genera un nuevo número de comprobante para una factura
DELIMITER $$
CREATE FUNCTION generarNumeroComprobante()
    RETURNS VARCHAR(255)
    DETERMINISTIC
BEGIN
    DECLARE _ultimoNro INT DEFAULT 0;

    SELECT IFNULL(CAST(nroComprobante AS UNSIGNED), 0)
    INTO _ultimoNro
    FROM factura
    ORDER BY idFactura DESC
    LIMIT 1;

    RETURN LPAD(_ultimoNro + 1, 8, '0');
END $$