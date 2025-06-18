package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Usuario.DireccionDto;
import com.example.demo.Domain.Entities.Direccion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DireccionMapper {
    Direccion direccionDtoToDireccion(DireccionDto dto);
}

