package com.example.demo.Application.DTO.Pedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Para cambiar el estado de un pedido cuando es confirmado o rechazado por el cajero
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CambioDeEstadoDto {
    private Long idPedido;
    private String estadoPedido;
}
