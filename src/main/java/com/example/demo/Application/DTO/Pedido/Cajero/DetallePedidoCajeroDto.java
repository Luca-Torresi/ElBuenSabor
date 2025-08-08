package com.example.demo.Application.DTO.Pedido.Cajero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class DetallePedidoCajeroDto {
    private Long idArticulo;
    private String nombreArticulo;
    private Long idPromocion;
    private String tituloPromocion;
    private Integer cantidad;
    private Double subtotal;
}
