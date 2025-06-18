package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Usuario.*;
import com.example.demo.Application.Mapper.ImagenMapper;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Entities.Direccion;
import com.example.demo.Domain.Entities.Empleado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ImagenMapper.class)
public interface UsuarioMapper {

    PerfilUsuarioDto clienteToPerfilUsuarioDto(Cliente cliente);

    @Mapping(source = "imagenModel", target = "imagen")
    Cliente registroClienteDtoToCliente(RegistroClienteDto dto);
    Empleado nuevoEmpleadoDtoToEmpleado(NuevoEmpleadoDto nuevoEmpleadoDto);

    @Mapping(target = "departamentoNombre", expression = "java(obtenerNombreDepartamento(empleado))")
    @Mapping(target = "activo", expression = "java(empleado.getFechaBaja() == null)")
    @Mapping(target = "calle", expression = "java(empleado.getDireccion() != null ? empleado.getDireccion().getCalle() : null)")
    @Mapping(target = "numero", expression = "java(empleado.getDireccion() != null ? empleado.getDireccion().getNumero() : null)")
    @Mapping(target = "piso", expression = "java(empleado.getDireccion() != null ? empleado.getDireccion().getPiso() : null)")
    @Mapping(target = "dpto", expression = "java(empleado.getDireccion() != null ? empleado.getDireccion().getDpto() : null)")
    EmpleadoResponseDto empleadoToEmpleadoResponseDto(Empleado empleado);

    default String obtenerNombreDepartamento(Empleado empleado) {
        if (empleado.getDireccion() != null && empleado.getDireccion().getDepartamento() != null) {
            return empleado.getDireccion().getDepartamento().getNombre();
        }
        return null;
    }

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    void updateEmpleadoFromDto(NuevoEmpleadoDto dto, @MappingTarget Empleado empleado);

    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "fechaBaja", ignore = true)
    void updateEmpleadoFromDto(ActualizarEmpleadoDto dto, @MappingTarget Empleado empleado);

    @Mappings({
            @Mapping(target = "idDireccion", source = "idDireccion"),
            @Mapping(target = "calle", source = "calle"),
            @Mapping(target = "numero", source = "numero"),
            @Mapping(target = "piso", source = "piso"),
            @Mapping(target = "dpto", source = "dpto"),
            @Mapping(target = "departamento.idDepartamento", source = "idDepartamento")
    })
    Direccion direccionDtoToDireccion(DireccionDto direccionDto);
}
