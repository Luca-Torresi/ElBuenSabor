package com.example.demo.Application.DTO.Pedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Detalles sobre nuevo pedido
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class DetallePedidoDto {
    private Long idArticulo;
    private int cantidad;
}
