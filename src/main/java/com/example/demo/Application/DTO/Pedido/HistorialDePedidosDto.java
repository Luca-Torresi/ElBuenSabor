package com.example.demo.Application.DTO.Pedido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

//Devuelve un arrelgo con todos los pedidos realizados por un cliente
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class HistorialDePedidosDto {
    private List<PedidoDto> pedidos;
}
