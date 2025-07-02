package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Pedido.NuevoPedidoDto;
import com.example.demo.Application.DTO.Pedido.PedidoCajeroDto;
import com.example.demo.Application.DTO.Pedido.PedidoCocinaDto;
import com.example.demo.Application.DTO.Pedido.PedidoRepartidorDto;
import com.example.demo.Domain.Entities.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DetallePedidoMapper.class)
public interface PedidoMapper {
    NuevoPedidoDto pedidoToNuevoPedidoDto(Pedido pedido);

    @Mapping(source = "cliente.email", target = "emailCliente")
    PedidoCajeroDto pedidoToPedidoDto(Pedido pedido);

    PedidoCocinaDto pedidoToPedidoCocinaDto(Pedido pedido);

    PedidoRepartidorDto pedidoToPedidoRepartidorDto(Pedido pedido);
}
