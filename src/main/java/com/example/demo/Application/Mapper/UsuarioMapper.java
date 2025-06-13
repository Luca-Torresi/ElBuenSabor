package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Usuario.NuevoEmpleadoDto;
import com.example.demo.Application.DTO.Usuario.PerfilUsuarioDto;
import com.example.demo.Application.DTO.Usuario.RegistroClienteDto;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Entities.Empleado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ImagenMapper.class)
public interface UsuarioMapper {
    PerfilUsuarioDto clienteToPerfilUsuarioDto(Cliente cliente);

    @Mapping(source = "imagenModel", target = "imagen")
    Cliente registroClienteDtoToCliente(RegistroClienteDto dto);
    Empleado nuevoEmpleadoDtoToEmpleado(NuevoEmpleadoDto nuevoEmpleadoDto);
}
