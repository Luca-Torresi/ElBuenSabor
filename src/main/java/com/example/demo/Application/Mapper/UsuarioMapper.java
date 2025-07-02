// src/main/java/com/example/demo/Application/Mapper/UsuarioMapper.java
package com.example.demo.Application.Mapper;

// AQUI ESTAN TUS IMPORTS. REVISA CADA UNO.
import com.example.demo.Application.DTO.Usuario.ActualizarEmpleadoDto;
import com.example.demo.Application.DTO.Direccion.DireccionDto;
import com.example.demo.Application.DTO.Usuario.EmpleadoResponseDto;
import com.example.demo.Application.DTO.Usuario.NuevoEmpleadoDto;
import com.example.demo.Application.DTO.Usuario.PerfilUsuarioDto;
import com.example.demo.Application.DTO.Usuario.RegistroClienteDto;
// Importaciones de los nuevos DTOs de dirección y departamento
import com.example.demo.Application.DTO.Direccion.DireccionResponseDto; // <-- NUEVO DTO de respuesta
import com.example.demo.Application.DTO.Direccion.DepartamentoDto; // <-- DTO de Departamento

import com.example.demo.Domain.Entities.*;

import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ImagenMapper.class)
public interface UsuarioMapper {

    PerfilUsuarioDto clienteToPerfilUsuarioDto(Cliente cliente);

    default List<String> mapRolesToStrings(Set<Rol> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(Rol::getNombre)
                .collect(Collectors.toList());
    }

    @Mapping(source = "imagenModel", target = "imagen")
    Cliente registroClienteDtoToCliente(RegistroClienteDto dto);

    Empleado nuevoEmpleadoDtoToEmpleado(NuevoEmpleadoDto nuevoEmpleadoDto);


    // --- MÉTODO EMPLEADOTOEMPLEADORESPONSEDTO ACTUALIZADO ---
    @Mapping(target = "auth0Id", source = "idAuth0")
    @Mapping(target = "rol", source = "rol.nombre")
    @Mapping(target = "fechaBaja", source = "fechaBaja")
    @Mapping(target = "imagen", source = "imagen", qualifiedByName = "imagenToUrl")
    EmpleadoResponseDto empleadoToEmpleadoResponseDto(Empleado empleado);

    @Named("imagenToUrl")
    default String imagenToUrl(Imagen imagen) {
        return imagen != null ? imagen.getUrl() : null;
    }


    // --- MÉTODOS DEFAULT PARA MAPEOS ANIDADOS ---

    // Método para mapear Entidad Direccion a DireccionResponseDto
    @Named("toDireccionResponseDto") // <-- Nombre para referenciar desde @Mapping
    default DireccionResponseDto toDireccionResponseDto(Direccion direccion) {
        if (direccion == null) {
            return null;
        }
        Departamento dep = direccion.getDepartamento();
        return DireccionResponseDto.builder()
                .idDireccion(direccion.getIdDireccion())
                .calle(direccion.getCalle())
                .numero(direccion.getNumero())
                .piso(direccion.getPiso())
                .dpto(direccion.getDpto())
                .departamento(toDepartamentoDto(dep)) // <-- Llama al mapeo de Departamento
                .build();
    }

    // Método para mapear Entidad Departamento a DepartamentoDto
    @Named("toDepartamentoDto") // <-- Nombre para referenciar
    default DepartamentoDto toDepartamentoDto(Departamento departamento) {
        if (departamento == null) {
            return null;
        }
        // Asumiendo que DepartamentoDto solo tiene id y nombre
        return DepartamentoDto.builder()
                .id(departamento.getIdDepartamento())
                .nombre(departamento.getNombre())
                // Si DepartamentoDto tiene provincia, se mapea aquí:
                // .nombreProvincia(departamento.getProvincia() != null ? departamento.getProvincia().getNombre() : null)
                .build();
    }
    // ------------------------------------------


    // Métodos de actualización (mantienen su lógica, solo revisa 'direccion')
    // El campo 'direccion' en Empleado se actualizará desde 'direccion' del DTO,
    // pero MapStruct no hará la lógica de búsqueda/creación de dirección.
    // Esa lógica debe estar en ServiceEmpleado como la implementaste.
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "idAuth0", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "imagen", ignore = true)
    void updateEmpleadoFromDto(NuevoEmpleadoDto dto, @MappingTarget Empleado empleado);

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "idAuth0", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "imagen", ignore = true)
    void updateEmpleadoFromDto(ActualizarEmpleadoDto dto, @MappingTarget Empleado empleado);

    // Este mapeo DireccionDto a Direccion es útil si lo usas en otros lugares,
    // pero recuerda que DireccionDto tiene ID y NuevaDireccionDto no.
    // Si DireccionDto se usa solo para el PUT de empleado, la entidad 'Direccion'
    // se buscará por ID en ServiceEmpleado y se actualizará allí.
    @Mappings({
            @Mapping(target = "idDireccion", source = "idDireccion"),
            @Mapping(target = "departamento", ignore = true) // <-- Ignorar aquí
    })
    Direccion direccionDtoToDireccion(DireccionDto direccionDto);
}