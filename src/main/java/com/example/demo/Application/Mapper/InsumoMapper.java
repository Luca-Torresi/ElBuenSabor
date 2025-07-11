package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.ArticuloInsumo.InformacionInsumoDto;
import com.example.demo.Application.DTO.ArticuloInsumo.NuevoInsumoDto;
import com.example.demo.Domain.Entities.ArticuloInsumo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InsumoMapper {
    ArticuloInsumo articuloInsumoDtoToArticuloInsumo(NuevoInsumoDto nuevoInsumoDto);

    @Mapping(source = "rubroInsumo.idRubroInsumo", target = "idRubro")
    @Mapping(source = "rubroInsumo.nombre", target = "nombreRubro")
    @Mapping(source = "unidadDeMedida.idUnidadDeMedida", target = "idUnidadDeMedida")
    @Mapping(source = "unidadDeMedida.nombre", target = "unidadDeMedida")
    InformacionInsumoDto articuloInsumoToInformacionInsumoDto(ArticuloInsumo articuloInsumo);
}
