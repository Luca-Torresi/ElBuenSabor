package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Usuario.PerfilUsuarioDto;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Entities.Empleado;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    PerfilUsuarioDto clienteToPerfilUsuarioDto(Cliente cliente);
    PerfilUsuarioDto empleadoToPerfilUsuarioDto(Empleado empleado);
}
