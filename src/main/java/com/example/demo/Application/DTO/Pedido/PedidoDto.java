package com.example.demo.Application.DTO.Pedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PedidoDto {
    private Long idPedido;
    private LocalDateTime fechaYHora;
    private String tipoEnvio;
    private String metodoDePago;
    private String estadoPedido;
    private String emailCliente;
    private List<DetallePedidoDto> detalles;
}
