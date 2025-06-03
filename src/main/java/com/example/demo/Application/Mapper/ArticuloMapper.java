package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Articulo.ArticuloDto;
import com.example.demo.Domain.Entities.Articulo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ImagenMapper.class)
public interface ArticuloMapper {
    @Mapping(source = "imagen", target = "imagenDto")
    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    ArticuloDto articuloToArticuloDto(Articulo articulo);
}
