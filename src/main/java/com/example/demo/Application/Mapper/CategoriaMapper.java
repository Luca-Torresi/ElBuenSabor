package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Categoria.CategoriaDto;
import com.example.demo.Application.DTO.Categoria.NuevaCategoriaDto;
import com.example.demo.Domain.Entities.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ImagenMapper.class)
public interface CategoriaMapper {
    @Mapping(source = "imagenModel", target = "imagen")
    Categoria nuevaCategoriaDtoToCategoria(NuevaCategoriaDto nuevaCategoriaDto);

    @Mapping(source = "imagen", target = "imagenModel")
    @Mapping(source = "categoriaPadre.idCategoria", target = "idCategoriaPadre")
    CategoriaDto categoriaToCategoriaDto(Categoria categoria);
}
