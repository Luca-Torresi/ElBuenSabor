package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Generic.ItemDTO;
import com.example.demo.Domain.Entities.Pedido;
import com.example.demo.Domain.Enums.EstadoPedido;
import com.example.demo.Domain.Repositories.RepoPedido;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MercadoPagoService {

    @Value("${mercadopago.access.token}")
    private String mercadoPagoAccessToken;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    private final RepoPedido repoPedido;
    private static final Logger logger = LoggerFactory.getLogger(MercadoPagoService.class);

    @PostConstruct
    public void initialize() {
        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);
        } catch (Exception e) {
            logger.error("Error al inicializar MercadoPago SDK: {}", e.getMessage());
        }
    }

    public String createPreference(List<ItemDTO> items, String externalReference) throws MPException, MPApiException {
        try {
            PreferenceClient client = new PreferenceClient();

            List<PreferenceItemRequest> preferenceItems = new ArrayList<>();
            for (ItemDTO itemDTO : items) {
                PreferenceItemRequest item = PreferenceItemRequest.builder()
                        .title(itemDTO.getTitle())
                        .quantity(itemDTO.getQuantity())
                        .unitPrice(BigDecimal.valueOf(itemDTO.getUnitPrice()))
                        .build();
                preferenceItems.add(item);
            }

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("https://localhost:5173/pago/success")
                    .pending("https://localhost:5173/pago/pending")
                    .failure("https://localhost:5173/pago/failure")
                    .build();

            PreferenceRequest.PreferenceRequestBuilder builder = PreferenceRequest.builder()
                    .items(preferenceItems)
                    .backUrls(backUrls)
                    .autoReturn("all"); // redirige automáticamente en approved, pending o failure

            if (externalReference != null && !externalReference.isEmpty()) {
                builder.externalReference(externalReference);
            }

            PreferenceRequest preferenceRequest = builder.build();

            return client.create(preferenceRequest).getInitPoint();

        } catch (MPApiException e) {
            logger.error("Error de API Mercado Pago: {} - {}",
                    e.getApiResponse().getStatusCode(),
                    e.getApiResponse().getContent()); // <- muestra el JSON con el detalle del error
            throw e;
        } catch (MPException e) {
            logger.error("Error SDK Mercado Pago: {}", e.getMessage());
            throw e;
        }

    }

    public void processPaymentWebhook(Long paymentId) throws MPException, MPApiException {
        logger.info("Procesando webhook para payment ID: {}", paymentId);

        try {
            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(paymentId);

            String externalReference = payment.getExternalReference();
            if (externalReference == null || externalReference.isEmpty()) {
                logger.warn("El pago {} no tiene referencia externa", paymentId);
                return;
            }

            logger.info("Pago {} corresponde al pedido con ID: {}", paymentId, externalReference);

            Long pedidoId;
            try {
                pedidoId = Long.parseLong(externalReference);
            } catch (NumberFormatException e) {
                logger.error("La referencia externa no es un ID válido: {}", externalReference);
                return;
            }

            Optional<Pedido> pedidoOpt = repoPedido.findById(pedidoId);
            if (pedidoOpt.isEmpty()) {
                logger.error("No se encontró el pedido con ID: {}", pedidoId);
                return;
            }

            Pedido pedido = pedidoOpt.get();
            String status = payment.getStatus();

            logger.info("Estado del pago {}: {}", paymentId, status);

            switch (status) {
                case "approved" -> {
                    pedido.setEstadoPedido(EstadoPedido.A_CONFIRMAR);
                    logger.info("Pedido {} marcado como A_CONFIRMAR", pedidoId);
                }
                case "rejected" -> {
                    pedido.setEstadoPedido(EstadoPedido.RECHAZADO);
                    logger.info("Pedido {} marcado como RECHAZADO", pedidoId);
                }
                case "cancelled" -> {
                    pedido.setEstadoPedido(EstadoPedido.CANCELADO);
                    logger.info("Pedido {} marcado como CANCELADO", pedidoId);
                }
                case "pending" -> logger.info("Pedido {} mantiene estado actual por pago pendiente", pedidoId);
                default -> logger.warn("Estado de pago no manejado: {}", status);
            }

            repoPedido.save(pedido);
            logger.info("Pedido {} actualizado correctamente", pedidoId);

        } catch (MPApiException e) {
            logger.error("Error de API Mercado Pago: {} - {}", e.getApiResponse().getStatusCode(), e.getApiResponse().getContent());
            throw e;
        } catch (MPException e) {
            logger.error("Error SDK Mercado Pago: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado al procesar webhook: {}", e.getMessage());
            throw new RuntimeException("Error al procesar webhook", e);
        }
    }
}
