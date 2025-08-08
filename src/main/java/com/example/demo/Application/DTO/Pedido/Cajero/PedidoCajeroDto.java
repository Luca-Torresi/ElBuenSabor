package com.example.demo.Application.DTO.Pedido.Cajero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

//Contiene los datos relevantes para ser mostrados en el panel de gestión de pedidos del cajero
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PedidoCajeroDto {
    private Long idPedido;
    private LocalDateTime fechaYHora;
    private String tipoEnvio;
    private String metodoDePago;
    private Double total;
    private String estadoPedido;
    private String emailCliente;
    private List<DetallePedidoCajeroDto> detalles;
}