package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Pedido.DetallePedidoCajeroDto;
import com.example.demo.Application.DTO.Pedido.DetallePedidoCocinaDto;
import com.example.demo.Domain.Entities.DetallePedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetallePedidoMapper {
    @Mapping(source = "articulo.idArticulo", target = "idArticulo")
    @Mapping(source = "articulo.nombre", target = "nombreArticulo")
    DetallePedidoCajeroDto detallePedidoToDetallePedidoDto(DetallePedido detallePedido);

    @Mapping(source = "articulo.nombre", target = "nombreArticulo")
    DetallePedidoCocinaDto detallePedidoToDetallePedidoCocinaDto(DetallePedido detallePedido);
}
