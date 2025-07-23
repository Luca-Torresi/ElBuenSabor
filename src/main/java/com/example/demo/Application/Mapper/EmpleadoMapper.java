package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Usuario.RepartidorDto;
import com.example.demo.Domain.Entities.Empleado;

public interface EmpleadoMapper {

    RepartidorDto empleadoToRepartidorDto(Empleado empleado);
}
