package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Usuario.PerfilClienteDto;
import com.example.demo.Application.DTO.Usuario.PerfilEmpleadoDto;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Entities.Empleado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    PerfilClienteDto clienteToPerfilClienteDto(Cliente cliente);

    PerfilEmpleadoDto empleadoToPerfilEmpleadoDto(Empleado empleado);
}
