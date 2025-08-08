package com.example.demo.Application.DTO.Pedido.Cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class DetallePedidoClienteDto {
    private String nombreArticulo;
    private String tituloPromocion;
    private Integer cantidad;
    private Double subtotal;
}
