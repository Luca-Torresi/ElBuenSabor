package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.RubroInsumo.RubroInsumoCompletoDto;
import com.example.demo.Domain.Entities.RubroInsumo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RubroInsumoMapper {

    @Mapping(source = "rubroInsumoPadre.idRubroInsumo", target = "idRubroPadre")
    @Mapping(source = "rubroInsumoPadre.nombre", target = "rubroPadre")
    @Mapping(target = "insumos", ignore = true)
    RubroInsumoCompletoDto rubroInsumoToRubroInsumoCompletoDto(RubroInsumo rubroInsumo);
}
