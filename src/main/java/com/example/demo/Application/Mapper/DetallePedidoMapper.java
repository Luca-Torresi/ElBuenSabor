package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Pedido.Cajero.DetallePedidoCajeroDto;
import com.example.demo.Application.DTO.Pedido.Cliente.DetallePedidoClienteDto;
import com.example.demo.Application.DTO.Pedido.Cocinero.DetallePedidoCocinaDto;
import com.example.demo.Domain.Entities.DetallePedido;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = PromocionItemMapper.class)
public interface DetallePedidoMapper {

    @Mapping(source = "articulo.nombre", target = "nombreArticulo")
    @Mapping(source = "promocion.titulo", target = "tituloPromocion")
    DetallePedidoCajeroDto detallePedidoToDetallePedidoDto(DetallePedido detallePedido);

    @Mapping(source = "articulo.nombre", target = "nombreArticulo")
    @Mapping(source = "promocion.titulo", target = "tituloPromocion")
    @Mapping(source = "promocion.detalles", target = "detallesPromocion")
    DetallePedidoCocinaDto detallePedidoToDetallePedidoCocinaDto(DetallePedido detallePedido);

    @Mapping(source = "articulo.nombre", target = "nombreArticulo")
    @Mapping(source = "promocion.titulo", target = "tituloPromocion")
    DetallePedidoClienteDto detallePedidoToDetallePedidoClienteDto(DetallePedido detallePedido);

}
