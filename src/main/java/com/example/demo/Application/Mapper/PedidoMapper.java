package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Pedido.Cajero.PedidoCajeroDto;
import com.example.demo.Application.DTO.Pedido.Cliente.NuevoPedidoDto;
import com.example.demo.Application.DTO.Pedido.Cliente.PedidoClienteDto;
import com.example.demo.Application.DTO.Pedido.Cocinero.PedidoCocinaDto;
import com.example.demo.Application.DTO.Pedido.Repartidor.PedidoRepartidorDto;
import com.example.demo.Domain.Entities.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DetallePedidoMapper.class, DireccionMapper .class, EmpleadoMapper.class})
public interface PedidoMapper {
    NuevoPedidoDto pedidoToNuevoPedidoDto(Pedido pedido);

    @Mapping(source = "cliente.email", target = "emailCliente")
    PedidoCajeroDto pedidoToPedidoDto(Pedido pedido);

    PedidoCocinaDto pedidoToPedidoCocinaDto(Pedido pedido);

    PedidoRepartidorDto pedidoToPedidoRepartidorDto(Pedido pedido);

    PedidoClienteDto pedidoToPedidoClienteDto(Pedido pedido);
}
