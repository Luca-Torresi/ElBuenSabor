package com.example.demo.Application.DTO.Pedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

//Información necesaria sobre un nuevo pedido
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PedidoDto {
    private String tipoEnvio;
    private String estadoPedido;
    private String metodoDePago;
    private List<DetallePedidoDto> detalles;
}
