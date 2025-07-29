package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Generic.ItemDTO;
import com.example.demo.Application.DTO.Generic.PreferenciaPagoDto;
import com.example.demo.Domain.Service.MercadoPagoService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Application.DTO.MercadoPago.WebhookNotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PagoController {

    private final MercadoPagoService mercadoPagoService;
    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);

    @PostMapping("/api/pagos/preferencia")
    public ResponseEntity<?> generarPreferencia(@RequestBody PreferenciaPagoDto preferenciaPagoDto) {
        try {
            List<ItemDTO> items = preferenciaPagoDto.getItems();
            Double envio = preferenciaPagoDto.getCostoEnvio();

            if (items == null || items.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "La lista de items no puede estar vacía"));
            }
            double costoEnvio = (envio != null) ? envio : 0.0;

            String initPoint = mercadoPagoService.createPreference(items, null, costoEnvio);

            return ResponseEntity.ok(Map.of("init_point", initPoint));

        } catch (MPApiException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error de API de Mercado Pago",
                            "status", e.getApiResponse().getStatusCode(),
                            "message", e.getApiResponse().getContent()
                    ));
        } catch (MPException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error de Mercado Pago: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar el pago: " + e.getMessage()));
        }
    }

    /*
    // Webhook para recibir notificaciones de Mercado Pago (implementación futura)
    @GetMapping("/webhook")
    public ResponseEntity<?> handleWebhook(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String data_id,
            @RequestParam(required = false) String external_reference) {

        // Aquí se procesaría la notificación de Mercado Pago
        // Por ahora solo registramos la llamada
        System.out.println("Webhook recibido - Type: " + type + ", Data ID: " + data_id + ", External Reference: " + external_reference);

        return ResponseEntity.ok().build();
    }
    */
    @PostMapping("/api/pagos/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody WebhookNotificationDTO notification) {
        logger.info("Webhook recibido: {}", notification);

        // Validar que data.id exista
        if (notification.getData() == null || notification.getData().getId() == null) {
            logger.warn("Webhook recibido sin data.id");
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "El campo data.id es requerido"));
        }

        Long paymentId = notification.getData().getId();
        logger.info("Procesando notificación para payment_id: {}", paymentId);

        try {
            // Procesar la notificación de pago
            mercadoPagoService.processPaymentWebhook(paymentId);

            // Responder con éxito
            return ResponseEntity.ok(Map.of("status", "success"));

        } catch (MPApiException e) {
            logger.error("Error de API de Mercado Pago: {} - {}",
                    e.getApiResponse().getStatusCode(), e.getApiResponse().getContent());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error de API de Mercado Pago",
                            "status", e.getApiResponse().getStatusCode(),
                            "message", e.getApiResponse().getContent()
                    ));
        } catch (MPException e) {
            logger.error("Error de Mercado Pago: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error de Mercado Pago: " + e.getMessage()));
        } catch (Exception e) {
            logger.error("Error inesperado al procesar el webhook: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar el webhook: " + e.getMessage()));
        }
    }
}
