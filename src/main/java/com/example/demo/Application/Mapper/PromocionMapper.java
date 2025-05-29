package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Promocion.PromocionDto;
import com.example.demo.Domain.Entities.Promocion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromocionMapper {
    Promocion promocionDtoToPromocion(PromocionDto promocionDto);
}
