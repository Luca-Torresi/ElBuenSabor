package com.example.demo.Application.DTO.Pedido.Cliente;

import com.example.demo.Application.DTO.Direccion.DireccionDto;
import com.example.demo.Application.DTO.Usuario.RepartidorDto;
import com.example.demo.Domain.Enums.EstadoPedido;
import com.example.demo.Domain.Enums.MetodoDePago;
import com.example.demo.Domain.Enums.TipoEnvio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PedidoClienteDto {
    private Long idPedido;
    private LocalDateTime fechaYHora;
    private LocalDateTime horaEntrega;
    private EstadoPedido estadoPedido;
    private TipoEnvio tipoEnvio;
    private MetodoDePago metodoDePago;
    private List<DetallePedidoClienteDto> detalles;
    private DireccionDto direccion;
    private RepartidorDto Repartidor;
}
