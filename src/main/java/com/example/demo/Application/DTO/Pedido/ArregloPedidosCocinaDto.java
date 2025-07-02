package com.example.demo.Application.DTO.Pedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//Utilizado para enviar todos que se encuentran en preparación para el panel de gestión de pedidos del cocinero
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArregloPedidosCocinaDto {
    private List<PedidoCocinaDto> pedidos;
}

