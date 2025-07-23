package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Pedido.*;
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
