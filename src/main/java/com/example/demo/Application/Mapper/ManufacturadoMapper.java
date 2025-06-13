package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.ArticuloManufacturado.InformacionArticuloManufacturadoDto;
import com.example.demo.Application.DTO.ArticuloManufacturado.NuevoArticuloManufacturadoDto;
import com.example.demo.Domain.Entities.ArticuloManufacturado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ImagenMapper.class, ManufacturadoDetalleMapper.class})
public interface ManufacturadoMapper {
    @Mapping(source = "imagenModel", target = "imagen")
    ArticuloManufacturado nuevoArticuloManufacturadoDtoToArticuloManufacturado(NuevoArticuloManufacturadoDto nuevoArticuloManufacturadoDto);

    @Mapping(source = "imagen", target = "imagenModel")
    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    @Mapping(source = "categoria.nombre", target = "nombreCategoria")
    InformacionArticuloManufacturadoDto articuloManufacturadoToInformacionArticuloManufacturadoDto(ArticuloManufacturado articuloManufacturado);
}
