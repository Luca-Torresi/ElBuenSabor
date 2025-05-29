package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.ArticuloManufacturado.ArticuloManufacturadoDto;
import com.example.demo.Application.DTO.ArticuloManufacturado.InformacionArticuloManufacturadoDto;
import com.example.demo.Application.DTO.ArticuloManufacturado.ManufacturadoAbmDto;
import com.example.demo.Application.DTO.ArticuloManufacturado.NuevoArticuloManufacturadoDto;
import com.example.demo.Domain.Entities.ArticuloManufacturado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImagenMapper.class, ManufacturadoDetalleMapper.class})
public interface ManufacturadoMapper {
    @Mapping(source = "imagenDto", target = "imagenManufacturado")
    ArticuloManufacturado nuevoArticuloManufacturadoDtoToArticuloManufacturado(NuevoArticuloManufacturadoDto nuevoArticuloManufacturadoDto);

    @Mapping(source = "imagenManufacturado", target = "imagenDto")
    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    ArticuloManufacturadoDto articuloManufacturadoToArticuloManufacturadoDto(ArticuloManufacturado articuloManufacturado);

    @Mapping(source = "imagenManufacturado", target = "imagenDto")
    ManufacturadoAbmDto articuloManufacturadoToManufacturadoAbmDto(ArticuloManufacturado articuloManufacturado);

    @Mapping(source = "imagenManufacturado", target = "imagenDto")
    @Mapping(source = "categoria.idCategoria", target = "idCategoria")
    InformacionArticuloManufacturadoDto articuloManufacturadoToInformacionArticuloManufacturadoDto(ArticuloManufacturado articuloManufacturado);
}
