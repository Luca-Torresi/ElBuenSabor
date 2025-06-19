// src/main/java/com/example/demo/Domain/Service/ServiceEmpleado.java
package com.example.demo.Domain.Service;

import com.auth0.json.mgmt.users.User;
import com.example.demo.Application.DTO.Departamento.DepartamentoDto;
import com.example.demo.Application.DTO.Usuario.ActualizarEmpleadoDto;
import com.example.demo.Application.DTO.Usuario.DireccionDto;
import com.example.demo.Application.DTO.Usuario.DireccionResponseDto;
import com.example.demo.Application.DTO.Usuario.EmpleadoResponseDto;
import com.example.demo.Application.DTO.Usuario.NuevoEmpleadoDto;
import com.example.demo.Application.DTO.Usuario.NuevaDireccionDto;
import com.example.demo.Application.DTO.Usuario.UsuarioDTO;
import com.example.demo.Domain.Entities.Departamento;
import com.example.demo.Domain.Entities.Direccion;
import com.example.demo.Domain.Entities.Empleado;
import com.example.demo.Domain.Entities.Provincia;
import com.example.demo.Domain.Entities.Roles;
import com.example.demo.Domain.Repositories.RepoDepartamento;
import com.example.demo.Domain.Repositories.RepoDireccion;
import com.example.demo.Domain.Repositories.RepoEmpleado;
import com.example.demo.Domain.Repositories.RepoRoles;
import com.example.demo.Domain.Service.Auth.UserAuth0Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceEmpleado {

    private final RepoEmpleado repoEmpleado;
    private final RepoRoles repoRoles;
    private final UserAuth0Service userAuth0Service;
    private final RepoDireccion repoDireccion;
    private final RepoDepartamento repoDepartamento;

    @Transactional
    public void cargarNuevoEmpleado(NuevoEmpleadoDto nuevoEmpleadoDto) {
        String auth0UserId = null; // Variable para almacenar el ID de Auth0

        try {
            // 1. Crear usuario en Auth0
            User auth0User = userAuth0Service.createUser(
                    UsuarioDTO.builder()
                            .email(nuevoEmpleadoDto.getEmail())
                            .password(nuevoEmpleadoDto.getPassword())
                            .nombre(nuevoEmpleadoDto.getNombre())
                            .apellido(nuevoEmpleadoDto.getApellido())
                            .nickName(nuevoEmpleadoDto.getNickName())
                            .build()
            );

            auth0UserId = auth0User.getId(); // Almacenar el ID de Auth0

            // 2. Asignar roles en Auth0
            if (nuevoEmpleadoDto.getRolesAuth0Ids() != null && !nuevoEmpleadoDto.getRolesAuth0Ids().isEmpty()) {
                userAuth0Service.assignRoles(auth0UserId, nuevoEmpleadoDto.getRolesAuth0Ids());
            }

            // 3. Obtener los objetos Roles para la base de datos local
            Set<Roles> rolesAsignadosBD = new HashSet<>();
            if (nuevoEmpleadoDto.getRolesAuth0Ids() != null && !nuevoEmpleadoDto.getRolesAuth0Ids().isEmpty()) {
                rolesAsignadosBD = nuevoEmpleadoDto.getRolesAuth0Ids().stream()
                        .map(auth0RoleId -> repoRoles.findByAuth0RoleId(auth0RoleId)
                                .orElseThrow(() -> new RuntimeException("Rol de Auth0 no encontrado en BD local: " + auth0RoleId)))
                        .collect(Collectors.toSet());
            }

            // --- 4. Manejar la Dirección de CREACIÓN (usa NuevaDireccionDto) ---
            Direccion direccionEmpleado = null;
            if (nuevoEmpleadoDto.getDireccion() != null) {
                NuevaDireccionDto nuevaDireccionDto = nuevoEmpleadoDto.getDireccion();

                if (nuevaDireccionDto.getIdDepartamento() == null) {
                    throw new IllegalArgumentException("El ID del departamento es obligatorio para crear una nueva dirección.");
                }
                Departamento departamento = repoDepartamento.findById(nuevaDireccionDto.getIdDepartamento())
                        .orElseThrow(() -> new RuntimeException("Departamento con ID " + nuevaDireccionDto.getIdDepartamento() + " no encontrado."));

                direccionEmpleado = Direccion.builder()
                        .calle(nuevaDireccionDto.getCalle())
                        .numero(nuevaDireccionDto.getNumero())
                        .piso(nuevaDireccionDto.getPiso())
                        .dpto(nuevaDireccionDto.getDpto())
                        .departamento(departamento)
                        .build();
                direccionEmpleado = repoDireccion.save(direccionEmpleado); // Guarda la nueva dirección
            }
            // ------------------------------------------------------------------

            // 5. Guardar empleado en la base de datos local
            Empleado nuevoEmpleado = Empleado.builder()
                    .idAuth0(auth0UserId)
                    .email(nuevoEmpleadoDto.getEmail())
                    .nombre(nuevoEmpleadoDto.getNombre())
                    .apellido(nuevoEmpleadoDto.getApellido())
                    .telefono(nuevoEmpleadoDto.getTelefono())
                    .roles(rolesAsignadosBD)
                    .activo(true)
                    .direccion(direccionEmpleado)
                    .build();

            repoEmpleado.save(nuevoEmpleado);

        } catch (Exception e) {
            System.err.println("Error al cargar nuevo empleado: " + e.getMessage());
            // Si hubo un error después de crear el usuario en Auth0, intentar eliminarlo de Auth0
            if (auth0UserId != null) {
                try {
                    userAuth0Service.deleteUser(auth0UserId);
                    System.err.println("Usuario de Auth0 con ID " + auth0UserId + " eliminado debido a un error en la BD local.");
                } catch (Exception auth0DeleteException) {
                    System.err.println("Error crítico: No se pudo eliminar el usuario de Auth0 con ID " + auth0UserId + " después de un fallo en la BD local: " + auth0DeleteException.getMessage());
                    // Considerar loguear esto en un sistema de monitoreo o alerta
                }
            }
            throw new RuntimeException("Error al cargar nuevo empleado: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void altaBajaEmpleado(Long idEmpleado) {
        Empleado empleado = repoEmpleado.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + idEmpleado));

        empleado.setActivo(!empleado.getActivo());
        // Establece fechaBaja si se desactiva
        if (!empleado.getActivo()) {
            empleado.setFechaBaja(LocalDate.now());
        } else {
            empleado.setFechaBaja(null); // Resetea si se activa de nuevo
        }
        repoEmpleado.save(empleado);

        try {
            userAuth0Service.blockUser(empleado.getIdAuth0(), !empleado.getActivo());
        } catch (Exception e) {
            System.err.println("Error al sincronizar estado de bloqueo en Auth0 para empleado " + empleado.getIdAuth0() + ": " + e.getMessage());
            throw new RuntimeException("Error al actualizar estado del empleado en Auth0.", e);
        }
    }

    @Transactional
    public void modificarEmpleado(Long id, ActualizarEmpleadoDto dto) {
        Empleado empleadoExistente = repoEmpleado.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));

        try {
            userAuth0Service.modifyUser(UsuarioDTO.builder()
                    .auth0Id(empleadoExistente.getIdAuth0())
                    .email(dto.getEmail())
                    .nombre(dto.getNombre())
                    .apellido(dto.getApellido())
                    .nickName(dto.getNickName())
                    .build());

            if (dto.getRolesAuth0Ids() != null) {
                userAuth0Service.updateRoles(empleadoExistente.getIdAuth0(), dto.getRolesAuth0Ids());
            }

            empleadoExistente.setEmail(dto.getEmail());
            empleadoExistente.setNombre(dto.getNombre());
            empleadoExistente.setApellido(dto.getApellido());
            empleadoExistente.setTelefono(dto.getTelefono());

            if (dto.getRolesAuth0Ids() != null) {
                Set<Roles> nuevosRolesBD = dto.getRolesAuth0Ids().stream()
                        .map(auth0RoleId -> repoRoles.findByAuth0RoleId(auth0RoleId)
                                .orElseThrow(() -> new RuntimeException("Rol de Auth0 no encontrado en BD local: " + auth0RoleId)))
                        .collect(Collectors.toSet());
                empleadoExistente.setRoles(nuevosRolesBD);
            }

            // --- Manejar la actualización de la Dirección (usa DireccionDto para actualización) ---
            if (dto.getDireccion() != null) {
                DireccionDto direccionUpdateDto = dto.getDireccion();

                if (direccionUpdateDto.getIdDireccion() == null || direccionUpdateDto.getIdDireccion() <= 0) {
                    throw new IllegalArgumentException("Para actualizar o reasignar una dirección, 'idDireccion' es obligatorio en el DTO de dirección.");
                }

                Direccion direccionAUsar = repoDireccion.findById(direccionUpdateDto.getIdDireccion())
                        .orElseThrow(() -> new RuntimeException("Dirección con ID " + direccionUpdateDto.getIdDireccion() + " no encontrada."));

                if (empleadoExistente.getDireccion() != null && empleadoExistente.getDireccion().getIdDireccion().equals(direccionAUsar.getIdDireccion())) {
                    Departamento departamento = repoDepartamento.findById(direccionUpdateDto.getIdDepartamento())
                            .orElseThrow(() -> new RuntimeException("Departamento con ID " + direccionUpdateDto.getIdDepartamento() + " no encontrado."));
                    empleadoExistente.getDireccion().setCalle(direccionUpdateDto.getCalle());
                    empleadoExistente.getDireccion().setNumero(direccionUpdateDto.getNumero());
                    empleadoExistente.getDireccion().setPiso(direccionUpdateDto.getPiso());
                    empleadoExistente.getDireccion().setDpto(direccionUpdateDto.getDpto());
                    empleadoExistente.getDireccion().setDepartamento(departamento);
                    repoDireccion.save(empleadoExistente.getDireccion());
                } else {
                    empleadoExistente.setDireccion(direccionAUsar);
                }
            } else {
                empleadoExistente.setDireccion(null);
            }
            // -------------------------------------------------------------

            repoEmpleado.save(empleadoExistente);

        } catch (Exception e) {
            System.err.println("Error al modificar empleado " + id + ": " + e.getMessage());
            throw new RuntimeException("Error al modificar empleado en Auth0 o DB.", e);
        }
    }

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
            Direccion d = empleado.getDireccion();
            Departamento dep = d.getDepartamento();
            Provincia prov = (dep != null) ? dep.getProvincia() : null;

            DepartamentoDto departamentoDto = null;
            if (dep != null) {
                departamentoDto = DepartamentoDto.builder()
                        .id(dep.getIdDepartamento())
                        .nombre(dep.getNombre())
                        .build();
            }

            direccionResponseDto = DireccionResponseDto.builder()
                    .idDireccion(d.getIdDireccion())
                    .calle(d.getCalle())
                    .numero(d.getNumero())
                    .piso(d.getPiso())
                    .dpto(d.getDpto())
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
                .roles(empleado.getRoles().stream()
                        .map(Roles::getName)
                        .collect(Collectors.toList()))
                .direccion(direccionResponseDto)
                .build();
    }
}