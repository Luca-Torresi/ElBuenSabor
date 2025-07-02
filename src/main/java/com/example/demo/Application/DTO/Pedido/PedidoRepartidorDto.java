package com.example.demo.Application.DTO.Pedido;

import com.example.demo.Application.DTO.Direccion.DireccionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//Contiene la información para el panel de gestión de pedidos del repartidor
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRepartidorDto {
    private Long idPedido;
    private LocalDateTime horaEntrega;
    private String estadoPedido;
    private DireccionDto direccion;
}
