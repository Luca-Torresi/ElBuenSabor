package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Pedido.DetallePedidoDto;
import com.example.demo.Domain.Entities.DetallePedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetallePedidoMapper {
    @Mapping(source = "articulo.idArticulo", target = "idArticulo")
    @Mapping(source = "articulo.nombre", target = "nombreArticulo")
    DetallePedidoDto detallePedidoToDetallePedidoDto(DetallePedido detallePedido);
}
