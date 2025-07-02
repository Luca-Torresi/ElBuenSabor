package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Direccion.DireccionDto;
import com.example.demo.Domain.Entities.Direccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DireccionMapper {
    Direccion direccionDtoToDireccion(DireccionDto dto);

    @Mapping(source = "departamento.nombre", target = "nombreDepartamento")
    DireccionDto direccionToDireccionDto(Direccion direccion);
}

