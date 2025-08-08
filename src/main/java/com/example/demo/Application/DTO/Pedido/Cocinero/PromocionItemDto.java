package com.example.demo.Application.DTO.Pedido.Cocinero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PromocionItemDto {
    private String nombreArticulo;
    private Integer cantidad;
}
