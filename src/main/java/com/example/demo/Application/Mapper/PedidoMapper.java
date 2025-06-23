package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Pedido.NuevoPedidoDto;
import com.example.demo.Application.DTO.Pedido.PedidoDto;
import com.example.demo.Domain.Entities.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DetallePedidoMapper.class)
public interface PedidoMapper {
    NuevoPedidoDto pedidoToNuevoPedidoDto(Pedido pedido);

    @Mapping(source = "cliente.email", target = "emailCliente")
    PedidoDto pedidoToPedidoDto(Pedido pedido);
}
