package com.example.demo.Application.DTO.MercadoPago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebhookNotificationDTO {
    private String action;
    private String api_version;
    private String type;
    private String date_created;
    private Long id;
    private WebhookDataDTO data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WebhookDataDTO {
        private Long id;
    }
}
