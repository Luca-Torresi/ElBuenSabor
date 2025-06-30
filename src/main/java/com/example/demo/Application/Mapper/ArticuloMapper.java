package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Articulo.ArticuloDto;
import com.example.demo.Domain.Entities.Articulo;
import com.example.demo.Domain.Entities.ArticuloManufacturado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = ImagenMapper.class)
public interface ArticuloMapper {

    @Mapping(source = "imagen.url", target = "imagenModel")
    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    @Mapping(target = "tiempoDeCocina", source = "articulo", qualifiedByName = "mapTiempoDeCocina")
    ArticuloDto articuloToArticuloDto(Articulo articulo);

    // Métodos condicionales para manejar la herencia (estos están bien)
    @Named("mapTiempoDeCocina")
    default int mapTiempoDeCocina(Articulo articulo) {
        if (articulo instanceof ArticuloManufacturado) {
            return ((ArticuloManufacturado) articulo).getTiempoDeCocina();
        }
        return 0;
    }
}
