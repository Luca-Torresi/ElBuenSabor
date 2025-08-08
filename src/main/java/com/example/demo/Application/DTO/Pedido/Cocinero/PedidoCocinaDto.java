package com.example.demo.Application.DTO.Pedido.Cocinero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

//Información de pedidos para ser mostrada en el panel de gestión de pedidos del cocinero
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PedidoCocinaDto {
    private Long idPedido;
    private LocalDateTime fechaYHora;
    private LocalDateTime horaEntrega;
    private String tipoEnvio;
    private String estadoPedido;
    private List<DetallePedidoCocinaDto> detalles;
}