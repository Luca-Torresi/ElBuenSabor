package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.ArticuloManufacturado.InformacionDetalleDto;
import com.example.demo.Domain.Entities.ArticuloManufacturadoDetalle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ManufacturadoDetalleMapper {
    @Mapping(source = "articuloInsumo.idArticuloInsumo", target = "idArticuloInsumo")
    @Mapping(source = "articuloInsumo.nombre", target = "nombreInsumo")
    @Mapping(source = "articuloInsumo.unidadDeMedida.nombre", target = "unidadDeMedida")
    InformacionDetalleDto detalleToInformacionDetalleDto(ArticuloManufacturadoDetalle detalle);
}
