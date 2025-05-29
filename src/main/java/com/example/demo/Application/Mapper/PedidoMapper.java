package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Pedido.PedidoDto;
import com.example.demo.Domain.Entities.Pedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PedidoMapper {
    PedidoDto pedidoToPedidoDto(Pedido pedido);
}
