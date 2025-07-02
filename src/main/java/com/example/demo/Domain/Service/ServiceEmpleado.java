package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Usuario.*;
import com.example.demo.Application.Mapper.UsuarioMapper;
import com.example.demo.Domain.Entities.*;
import com.example.demo.Domain.Repositories.*;
import com.example.demo.Domain.Service.Auth.UserAuth0Service;
import com.example.demo.Domain.Service.Auth.UserBBDDService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceEmpleado extends ServiceUsuario<Empleado> {

    private final RepoEmpleado repoEmpleado;
    private final UsuarioMapper usuarioMapper;

    // Constructor inyectando repositorios y servicios, pasando a la superclase los servicios comunes
    public ServiceEmpleado(UserAuth0Service userAuth0Service,
                           UserBBDDService userBBDDService,
                           RepoRol repoRol,
                           RepoEmpleado repoEmpleado, RepoImagen repoImagen, UsuarioMapper usuarioMapper) {
        super(userAuth0Service, userBBDDService, repoRol, repoImagen);
        this.repoEmpleado = repoEmpleado;
        this.usuarioMapper = usuarioMapper;
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
                            .build();

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

        return repoEmpleado.save(actualizado);
    }

    // Alta/baja empleado con manejo de fechaBaja y sincronización Auth0
    @Transactional
    public void altaBajaEmpleado(Long idEmpleado) throws Exception {
        Empleado empleado = repoEmpleado.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + idEmpleado));

        boolean activo = empleado.getFechaBaja() == null;

        empleado.setFechaBaja(activo ? LocalDate.now() : null);

        repoEmpleado.save(empleado);

        // Bloquear/desbloquear usuario en Auth0 (usa método del padre)
        // userAuth0Service.blockUser(empleado.getIdAuth0(), !activo);
    }

    // Métodos para obtener DTOs para la UI, sin tocar lógica Auth0
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDto> obtenerEmpleadosFormateados() {
        List<Empleado> empleados = repoEmpleado.findAll();
        return empleados.stream()
                .map(empleado -> {
                    // --- Forzar la inicialización de relaciones LAZY aquí ---
                    // Imagen:
                    if (empleado.getImagen() != null) {
                        empleado.getImagen().getUrl(); // Acceso para cargarla
                    }
                    // Roles:
                    if (empleado.getRol() != null) {
                        empleado.getRol(); // Acceso para cargar la colección
                    }
                    // ----------------------------------------------------
                    return usuarioMapper.empleadoToEmpleadoResponseDto(empleado);
                })
                .collect(Collectors.toList());
    }

    public EmpleadoResponseDto obtenerEmpleadoFormateadoPorId(Long id) {
        Optional<Empleado> empleadoOptional = repoEmpleado.findById(id);
        if (empleadoOptional.isEmpty()) {
            throw new RuntimeException("Empleado no encontrado.");
        }
        Empleado empleado = empleadoOptional.get();

        // --- Forzar la inicialización de relaciones LAZY aquí ---
        // Imagen:
        if (empleado.getImagen() != null) {
            empleado.getImagen().getUrl();
        }
        // Roles:
        if (empleado.getRol() != null) {
            empleado.getRol();
        }
        // ----------------------------------------------------
        return usuarioMapper.empleadoToEmpleadoResponseDto(empleado);
    }

    private EmpleadoResponseDto mapToEmpleadoResponseDto(Empleado empleado) {

        return EmpleadoResponseDto.builder()
                .idUsuario(empleado.getIdUsuario())
                .auth0Id(empleado.getIdAuth0())
                .email(empleado.getEmail())
                .nombre(empleado.getNombre())
                .apellido(empleado.getApellido())
                .telefono(empleado.getTelefono())
                .fechaBaja(empleado.getFechaBaja())
                .rol(empleado.getRol().getNombre())
                .build();
    }
}
