package com.example.demo.Application.DTO.Pedido.Cocinero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class DetallePedidoCocinaDto {
    private String nombreArticulo;
    private String tituloPromocion;
    private List<PromocionItemDto> detallesPromocion;
    private Integer cantidad;
}
