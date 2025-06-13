package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.ArticuloInsumo.NuevoInsumoDto;
import com.example.demo.Domain.Entities.ArticuloInsumo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ImagenMapper.class)
public interface InsumoMapper {
    @Mapping(source = "imagenModel", target = "imagen")
    ArticuloInsumo articuloInsumoDtoToArticuloInsumo(NuevoInsumoDto nuevoInsumoDto);
}
