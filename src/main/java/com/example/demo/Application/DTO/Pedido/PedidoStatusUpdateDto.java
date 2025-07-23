package com.example.demo.Application.DTO.Pedido;

import com.example.demo.Domain.Enums.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoStatusUpdateDto {
    private Long idPedido;
    private EstadoPedido estadoPedido;
    private Long clienteId;
    private LocalDateTime horaEntrega;
    private String mensajeActualizacion;
    private String nombreRepartidor;
    private Integer cantidadProductos;
}
