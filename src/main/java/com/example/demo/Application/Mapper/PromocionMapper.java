package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Promocion.PromocionDto;
import com.example.demo.Domain.Entities.Promocion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ImagenMapper.class)
public interface PromocionMapper {

    @Mapping(source = "imagenModel", target = "imagen")
    Promocion promocionDtoToPromocion(PromocionDto promocionDto);
}
