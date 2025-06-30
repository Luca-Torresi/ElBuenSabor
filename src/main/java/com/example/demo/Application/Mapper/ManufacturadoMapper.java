package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.ArticuloManufacturado.InformacionArticuloManufacturadoDto;
import com.example.demo.Application.DTO.ArticuloManufacturado.NuevoArticuloManufacturadoDto;
import com.example.demo.Domain.Entities.ArticuloManufacturado;
import com.example.demo.Domain.Entities.Imagen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {ManufacturadoDetalleMapper.class})
public interface ManufacturadoMapper {
    ArticuloManufacturado nuevoArticuloManufacturadoDtoToArticuloManufacturado(NuevoArticuloManufacturadoDto nuevoArticuloManufacturadoDto);

    @Mapping(source = "imagen", target = "imagenUrl", qualifiedByName = "imagenToUrlString")
    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    @Mapping(source = "categoria.nombre", target = "nombreCategoria")
    InformacionArticuloManufacturadoDto articuloManufacturadoToInformacionArticuloManufacturadoDto(ArticuloManufacturado articuloManufacturado);

    @Named("imagenToUrlString")
    default String imagenToUrlString(Imagen imagen) {
        return (imagen != null) ? imagen.getUrl() : null;
    }
}
