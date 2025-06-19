// src/main/java/com/example/demo/Application/Mapper/UsuarioMapper.java
package com.example.demo.Application.Mapper;

// AQUI ESTAN TUS IMPORTS. REVISA CADA UNO.
import com.example.demo.Application.DTO.Usuario.ActualizarEmpleadoDto;
import com.example.demo.Application.DTO.Usuario.DireccionDto;
import com.example.demo.Application.DTO.Usuario.EmpleadoResponseDto;
import com.example.demo.Application.DTO.Usuario.NuevoEmpleadoDto;
import com.example.demo.Application.DTO.Usuario.PerfilUsuarioDto;
import com.example.demo.Application.DTO.Usuario.RegistroClienteDto;
// Importaciones de los nuevos DTOs de dirección y departamento
import com.example.demo.Application.DTO.Usuario.DireccionResponseDto; // <-- NUEVO DTO de respuesta
import com.example.demo.Application.DTO.Departamento.DepartamentoDto; // <-- DTO de Departamento

import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Entities.Direccion;
import com.example.demo.Domain.Entities.Empleado;
import com.example.demo.Domain.Entities.Roles;
import com.example.demo.Domain.Entities.Departamento; // <-- Importa la entidad Departamento
import com.example.demo.Domain.Entities.Provincia; // <-- Importa la entidad Provincia si es necesaria para el mapeo del departamento

import org.mapstruct.*;

import java.time.LocalDateTime; // <-- Asegurate de importar si Empleado tiene fechaBaja
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ImagenMapper.class)
public interface UsuarioMapper {

    PerfilUsuarioDto clienteToPerfilUsuarioDto(Cliente cliente);

    default List<String> mapRolesToStrings(Set<Roles> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(Roles::getName)
                .collect(Collectors.toList());
    }

    @Mapping(source = "imagenModel", target = "imagen")
    Cliente registroClienteDtoToCliente(RegistroClienteDto dto);

    // Mapeo para NuevoEmpleadoDto a Empleado (sin cambios relevantes en esta sección por ahora)
    // MapStruct mapeará automáticamente 'direccion' si los nombres de los campos coinciden
    // o si tienes un método para mapear NuevaDireccionDto a Direccion.
    // Como NuevaDireccionDto no tiene id, y Direccion si lo tiene, necesitarás un @Mapping
    // o un método default para manejarlo.
    // PERO: Si tu ServiceEmpleado ya crea la entidad Direccion y la asigna, este mapeo aquí
    // puede que no sea para crear, sino solo para actualizar campos si NuevaDireccionDto
    // tiene campos que mapeen directamente a la entidad Direccion.
    @Mapping(target = "direccion", ignore = true) // <--- IGNORAR aquí si ServiceEmpleado maneja la creación
    Empleado nuevoEmpleadoDtoToEmpleado(NuevoEmpleadoDto nuevoEmpleadoDto);


    // --- MÉTODO EMPLEADOTOEMPLEADORESPONSEDTO ACTUALIZADO ---
    @Mapping(target = "auth0Id", source = "idAuth0")
    @Mapping(target = "roles", expression = "java(mapRolesToStrings(empleado.getRoles()))")
    @Mapping(target = "direccion", source = "empleado.direccion", qualifiedByName = "toDireccionResponseDto") // <-- ¡CAMBIO CLAVE!
    @Mapping(target = "fechaBaja", source = "fechaBaja") // <-- Mapea fechaBaja directamente
    EmpleadoResponseDto empleadoToEmpleadoResponseDto(Empleado empleado);


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
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "imagen", ignore = true)
    @Mapping(target = "direccion", ignore = true) // <-- Ignorar para que ServiceEmpleado la maneje
    void updateEmpleadoFromDto(NuevoEmpleadoDto dto, @MappingTarget Empleado empleado);

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "idAuth0", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    @Mapping(target = "fechaActualizacion", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "imagen", ignore = true)
    @Mapping(target = "direccion", ignore = true) // <-- Ignorar para que ServiceEmpleado la maneje
    void updateEmpleadoFromDto(ActualizarEmpleadoDto dto, @MappingTarget Empleado empleado);

    // Este mapeo DireccionDto a Direccion es útil si lo usas en otros lugares,
    // pero recuerda que DireccionDto tiene ID y NuevaDireccionDto no.
    // Si DireccionDto se usa solo para el PUT de empleado, la entidad 'Direccion'
    // se buscará por ID en ServiceEmpleado y se actualizará allí.
    @Mappings({
            @Mapping(target = "idDireccion", source = "idDireccion"),
            @Mapping(target = "calle", source = "calle"),
            @Mapping(target = "numero", source = "numero"),
            @Mapping(target = "piso", source = "piso"),
            @Mapping(target = "dpto", source = "dpto"),
            // El mapeo a la entidad Departamento es más complejo y generalmente
            // se maneja en el servicio, no en el mapper, ya que implica buscar en BD.
            @Mapping(target = "departamento", ignore = true) // <-- Ignorar aquí
    })
    Direccion direccionDtoToDireccion(DireccionDto direccionDto);
}