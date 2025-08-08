package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Pedido.Cocinero.PromocionItemDto;
import com.example.demo.Domain.Entities.DetallePromocion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PromocionItemMapper {

    @Mapping(source = "articulo.nombre", target = "nombreArticulo")
    PromocionItemDto detallePromocionToPromocionItemDto(DetallePromocion detallePromocion);
}
