package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.ArticuloNoElaborado.InformacionArticuloNoElaboradoDto;
import com.example.demo.Domain.Entities.ArticuloNoElaborado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ImagenMapper.class)
public interface NoElaboradoMapper {
    @Mapping(source = "imagenManufacturado", target = "imagenDto")
    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    @Mapping(source = "categoria.nombre", target = "nombreCategoria")
    InformacionArticuloNoElaboradoDto articuloNoElaboradoToDto(ArticuloNoElaborado articuloNoElaborado);
}
