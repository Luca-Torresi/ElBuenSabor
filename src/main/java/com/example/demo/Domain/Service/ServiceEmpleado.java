package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Departamento.DepartamentoDto;
import com.example.demo.Application.DTO.Usuario.*;
import com.example.demo.Domain.Entities.*;
import com.example.demo.Domain.Repositories.*;
import com.example.demo.Domain.Service.Auth.UserAuth0Service;
import com.example.demo.Domain.Service.Auth.UserBBDDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceEmpleado extends ServiceUsuario<Empleado> {

    private final RepoEmpleado repoEmpleado;
    private final RepoDireccion repoDireccion;
    private final RepoDepartamento repoDepartamento;

    // Constructor inyectando repositorios y servicios, pasando a la superclase los servicios comunes
    public ServiceEmpleado(UserAuth0Service userAuth0Service,
                           UserBBDDService userBBDDService,
                           RepoRoles repoRoles,
                           RepoEmpleado repoEmpleado,
                           RepoDireccion repoDireccion,
                           RepoDepartamento repoDepartamento) {
        super(userAuth0Service, userBBDDService, repoRoles);
        this.repoEmpleado = repoEmpleado;
        this.repoDireccion = repoDireccion;
        this.repoDepartamento = repoDepartamento;
    }

    // Método para crear empleado, reutilizando crearUsuarioDesdeDTO y agregando lógica de dirección
    @Transactional
    public Empleado cargarNuevoEmpleado(NuevoEmpleadoDto nuevoEmpleadoDto) {
        return crearUsuarioDesdeDTO(
                // Convertimos UsuarioDTO desde NuevoEmpleadoDto (sin dirección aún)
                UsuarioDTO.builder()
                        .email(nuevoEmpleadoDto.getEmail())
                        .password(nuevoEmpleadoDto.getPassword())
                        .nombre(nuevoEmpleadoDto.getNombre())
                        .apellido(nuevoEmpleadoDto.getApellido())
                        .telefono(nuevoEmpleadoDto.getTelefono())
                        .rolesAuth0Ids(nuevoEmpleadoDto.getRolesAuth0Ids())
                        .build(),

                // Factory para crear entidad Empleado a partir del DTO UsuarioDTO
                dto -> {
                    Empleado empleado = Empleado.builder()
                            .email(dto.getEmail())
                            .nombre(dto.getNombre())
                            .apellido(dto.getApellido())
                            .telefono(dto.getTelefono())
                            .activo(true)
                            .build();

                    // Manejo de dirección si viene en nuevoEmpleadoDto
                    if (nuevoEmpleadoDto.getDireccion() != null) {
                        NuevaDireccionDto nuevaDir = nuevoEmpleadoDto.getDireccion();
                        if (nuevaDir.getIdDepartamento() == null) {
                            throw new IllegalArgumentException("El ID del departamento es obligatorio para crear una nueva dirección.");
                        }
                        Departamento departamento = repoDepartamento.findById(nuevaDir.getIdDepartamento())
                                .orElseThrow(() -> new RuntimeException("Departamento con ID " + nuevaDir.getIdDepartamento() + " no encontrado."));
                        Direccion direccion = Direccion.builder()
                                .calle(nuevaDir.getCalle())
                                .numero(nuevaDir.getNumero())
                                .piso(nuevaDir.getPiso())
                                .dpto(nuevaDir.getDpto())
                                .departamento(departamento)
                                .build();
                        empleado.setDireccion(repoDireccion.save(direccion));
                    }
                    return empleado;
                }
        );
    }

    // Modificar empleado con DTO específico (con dirección)
    @Transactional
    public Empleado modificarEmpleado(Long id, ActualizarEmpleadoDto dto) {
        Empleado empleadoExistente = repoEmpleado.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));

        // Modificar usuario básico (Auth0 y BD) usando método de la superclase
        UsuarioDTO usuarioDto = UsuarioDTO.builder()
                .auth0Id(empleadoExistente.getIdAuth0())
                .email(dto.getEmail())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .telefono(dto.getTelefono())
                .rolesAuth0Ids(dto.getRolesAuth0Ids())
                .build();

        Empleado actualizado = modificarUsuario(empleadoExistente.getIdAuth0(), usuarioDto);

        // Manejar actualización o reasignación de dirección
        if (dto.getDireccion() != null) {
            DireccionDto dirDto = dto.getDireccion();
            if (dirDto.getIdDireccion() == null || dirDto.getIdDireccion() <= 0) {
                throw new IllegalArgumentException("Para actualizar o reasignar una dirección, 'idDireccion' es obligatorio.");
            }
            Direccion direccionAUsar = repoDireccion.findById(dirDto.getIdDireccion())
                    .orElseThrow(() -> new RuntimeException("Dirección con ID " + dirDto.getIdDireccion() + " no encontrada."));

            if (actualizado.getDireccion() != null && actualizado.getDireccion().getIdDireccion().equals(direccionAUsar.getIdDireccion())) {
                Departamento departamento = repoDepartamento.findById(dirDto.getIdDepartamento())
                        .orElseThrow(() -> new RuntimeException("Departamento con ID " + dirDto.getIdDepartamento() + " no encontrado."));
                actualizado.getDireccion().setCalle(dirDto.getCalle());
                actualizado.getDireccion().setNumero(dirDto.getNumero());
                actualizado.getDireccion().setPiso(dirDto.getPiso());
                actualizado.getDireccion().setDpto(dirDto.getDpto());
                actualizado.getDireccion().setDepartamento(departamento);
                repoDireccion.save(actualizado.getDireccion());
            } else {
                actualizado.setDireccion(direccionAUsar);
            }
        } else {
            actualizado.setDireccion(null);
        }

        return repoEmpleado.save(actualizado);
    }

    // Alta/baja empleado con manejo de fechaBaja y sincronización Auth0
    @Transactional
    public void altaBajaEmpleado(Long idEmpleado) throws Exception {
        Empleado empleado = repoEmpleado.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + idEmpleado));

        boolean nuevoEstado = !empleado.getActivo();
        empleado.setActivo(nuevoEstado);
        empleado.setFechaBaja(nuevoEstado ? null : LocalDate.now());

        repoEmpleado.save(empleado);

        // Bloquear/desbloquear usuario en Auth0 (usa método del padre)
        userAuth0Service.blockUser(empleado.getIdAuth0(), !nuevoEstado);
    }

    // Métodos para obtener DTOs para la UI, sin tocar lógica Auth0
    public List<EmpleadoResponseDto> obtenerEmpleadosFormateados() {
        return repoEmpleado.findByActivoTrue().stream()
                .map(this::mapToEmpleadoResponseDto)
                .collect(Collectors.toList());
    }

    public EmpleadoResponseDto obtenerEmpleadoFormateadoPorId(Long id) {
        Empleado empleado = repoEmpleado.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));
        return mapToEmpleadoResponseDto(empleado);
    }

    private EmpleadoResponseDto mapToEmpleadoResponseDto(Empleado empleado) {
        DireccionResponseDto direccionResponseDto = null;
        if (empleado.getDireccion() != null) {
            Departamento dep = empleado.getDireccion().getDepartamento();
            DepartamentoDto departamentoDto = null;
            if (dep != null) {
                departamentoDto = DepartamentoDto.builder()
                        .id(dep.getIdDepartamento())
                        .nombre(dep.getNombre())
                        .build();
            }

            direccionResponseDto = DireccionResponseDto.builder()
                    .idDireccion(empleado.getDireccion().getIdDireccion())
                    .calle(empleado.getDireccion().getCalle())
                    .numero(empleado.getDireccion().getNumero())
                    .piso(empleado.getDireccion().getPiso())
                    .dpto(empleado.getDireccion().getDpto())
                    .departamento(departamentoDto)
                    .build();
        }

        return EmpleadoResponseDto.builder()
                .idUsuario(empleado.getIdUsuario())
                .auth0Id(empleado.getIdAuth0())
                .email(empleado.getEmail())
                .nombre(empleado.getNombre())
                .apellido(empleado.getApellido())
                .telefono(empleado.getTelefono())
                .activo(empleado.getActivo())
                .fechaBaja(empleado.getFechaBaja())
                .roles(empleado.getRoles().stream().map(Roles::getName).collect(Collectors.toList()))
                .direccion(direccionResponseDto)
                .build();
    }
}
