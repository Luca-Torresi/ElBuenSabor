package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.ArticuloNoElaborado.InformacionArticuloNoElaboradoDto;
import com.example.demo.Application.DTO.ArticuloNoElaborado.NuevoArticuloNoElaboradoDto;
import com.example.demo.Domain.Entities.ArticuloNoElaborado;
import com.example.demo.Domain.Entities.Imagen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface NoElaboradoMapper {

    ArticuloNoElaborado nuevoArticuloNoElaboradoDtoToArticuloNoElaborado(NuevoArticuloNoElaboradoDto articuloNoElaboradoDto);

    @Mapping(target = "imagenUrl", source = "imagen.url")
    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    @Mapping(source = "categoria.nombre", target = "nombreCategoria")
    InformacionArticuloNoElaboradoDto articuloNoElaboradoToDto(ArticuloNoElaborado articuloNoElaborado);
}
