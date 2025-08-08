package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Promocion.NuevaPromocionDto;
import com.example.demo.Application.DTO.Promocion.PromocionAbmDto;
import com.example.demo.Application.DTO.Promocion.PromocionCatalogoDto;
import com.example.demo.Domain.Entities.Imagen;
import com.example.demo.Domain.Entities.Promocion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {DetallePromocionMapper.class, ImagenMapper.class})
public interface PromocionMapper {

    Promocion promocionDtoToPromocion(NuevaPromocionDto nuevaPromocionDto);

    void updateFromDto(NuevaPromocionDto dto, @MappingTarget Promocion entidad);

    @Mapping(source = "imagen.url", target = "url")
    PromocionCatalogoDto promocionToPromocionCatalogoDto(Promocion promocion);

    @Mapping(source = "imagen.url", target = "url")
    PromocionAbmDto promocionToPromocionAbmDto(Promocion promocion);
}
