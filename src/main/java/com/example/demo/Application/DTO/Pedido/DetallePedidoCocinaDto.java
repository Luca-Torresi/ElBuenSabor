package com.example.demo.Application.DTO.Pedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetallePedidoCocinaDto {
    private String nombreArticulo;
    private int cantidad;
}
