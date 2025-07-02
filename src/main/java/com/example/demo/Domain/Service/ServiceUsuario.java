package com.example.demo.Domain.Service;

import com.auth0.json.mgmt.users.User;
import com.example.demo.Application.DTO.Usuario.ClienteRegistroDto;
import com.example.demo.Application.DTO.Usuario.UsuarioDTO;
import com.example.demo.Domain.Entities.Imagen;
import com.example.demo.Domain.Entities.Rol;
import com.example.demo.Domain.Entities.Usuario;
import com.example.demo.Domain.Repositories.RepoImagen;
import com.example.demo.Domain.Repositories.RepoRol;
import com.example.demo.Domain.Service.Auth.UserAuth0Service;
import com.example.demo.Domain.Service.Auth.UserBBDDService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Clase abstracta que define la lógica común para cualquier entidad que herede de Usuario
 * (como Cliente o Empleado). Proporciona operaciones compartidas con Auth0 y la base de datos.
 */
@RequiredArgsConstructor
public abstract class ServiceUsuario<T extends Usuario> {

    protected final UserAuth0Service userAuth0Service;
    protected final UserBBDDService userBBDDService;
    protected final RepoRol repoRol;
    protected final RepoImagen repoImagen;

    /**
     * Crear usuario en Auth0, asignar roles y luego persistirlo en BBDD usando una factory.
     */
    @Transactional
    public T crearUsuarioDesdeDTO(UsuarioDTO dto, Function<UsuarioDTO, T> factoryEntidad) {
        try {
            // 1. Crear usuario en Auth0
            User auth0User = userAuth0Service.createUser(dto);
            String auth0Id = auth0User.getId();

            // 2. Asignar roles en Auth0
            if (dto.getRolesAuth0Ids() != null && !dto.getRolesAuth0Ids().isEmpty()) {
                userAuth0Service.assignRoles(auth0Id, dto.getRolesAuth0Ids());
            }

            // 3. Crear entidad específica (Cliente o Empleado)
            T nuevoUsuario = factoryEntidad.apply(dto);
            nuevoUsuario.setIdAuth0(auth0Id);
            nuevoUsuario.setEmail(dto.getEmail());
            nuevoUsuario.setNombre(dto.getNombre());
            nuevoUsuario.setApellido(dto.getApellido());
            nuevoUsuario.setTelefono(dto.getTelefono());

            if (dto.getRolesAuth0Ids() != null && !dto.getRolesAuth0Ids().isEmpty()) {
                Set<Rol> roles = dto.getRolesAuth0Ids().stream()
                        .map(roleId -> repoRol.findByAuth0RoleId(roleId)
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado en BD: " + roleId)))
                        .collect(Collectors.toSet());
                nuevoUsuario.setRol(roles.iterator().next());
            }

            // 4. Guardar en BD
            return (T) userBBDDService.save(nuevoUsuario);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear usuario: " + e.getMessage(), e);
        }
    }

    @Transactional
    public T crearClienteDesdeDTO(ClienteRegistroDto dto, Function<ClienteRegistroDto, T> factoryEntidad) {
        try {
            // 1. El usuario ya está en Auth0, obtenelo
            User auth0User = userAuth0Service.getUserById(dto.getAuth0Id());
            if (auth0User == null) {
                throw new RuntimeException("Usuario no encontrado en Auth0.");
            }

            // 2. Crear entidad específica (Cliente)
            T nuevoUsuario = factoryEntidad.apply(dto);
            nuevoUsuario.setIdAuth0(auth0User.getId());
            nuevoUsuario.setEmail(auth0User.getEmail() != null ? auth0User.getEmail() : dto.getEmail());
            nuevoUsuario.setNombre(dto.getNombre());
            nuevoUsuario.setApellido(dto.getApellido());
            nuevoUsuario.setTelefono(dto.getTelefono());

            // Lógica clave para la imagen de Auth0
            Imagen imagen = null;
            String auth0PictureUrl = auth0User.getPicture(); // URL de la imagen de Auth0
            String dtoImageUrl = dto.getImagen(); // URL que podría venir del frontend en el DTO

            if (auth0PictureUrl != null && !auth0PictureUrl.isEmpty()) {
                imagen = Imagen.builder()
                        .url(auth0PictureUrl)
                        .name(dto.getAuth0Id() + "_auth0_profile") // Un nombre descriptivo
                        .publicId("auth0_" + dto.getAuth0Id())
                        .build();
            } else if (dtoImageUrl != null && !dtoImageUrl.isEmpty()) {
                // Si Auth0 no dio imagen, pero el DTO la incluye (ej. por un valor por defecto del frontend)
                imagen = Imagen.builder()
                        .url(dtoImageUrl)
                        .name(dto.getAuth0Id() + "_default_profile")
                        .build();
            }

            if (imagen != null) {
                imagen = repoImagen.save(imagen);
                nuevoUsuario.setImagen(imagen);
            }

            return (T) userBBDDService.save(nuevoUsuario);

        } catch (Exception e) {
        e.printStackTrace(); // TEMPORAL: para ver stack trace real
        throw new RuntimeException("Error al registrar cliente ya autenticado: " + e.getMessage(), e);
    }

}


    /**
     * Modificar usuario tanto en Auth0 como en la base de datos.
     */
    @Transactional
    public T modificarUsuario(String auth0Id, UsuarioDTO dto) {
        try {
            // 1. Modificar en Auth0
            dto.setAuth0Id(auth0Id);
            userAuth0Service.modifyUser(dto);

            // 2. Actualizar roles en Auth0 si aplica
            if (dto.getRolesAuth0Ids() != null) {
                userAuth0Service.updateRoles(auth0Id, dto.getRolesAuth0Ids());
            }

            // 3. Actualizar en BD
            T usuarioExistente = (T) userBBDDService.findByIdAuth0(auth0Id);
            usuarioExistente.setNombre(dto.getNombre());
            usuarioExistente.setApellido(dto.getApellido());
            usuarioExistente.setEmail(dto.getEmail());
            usuarioExistente.setTelefono(dto.getTelefono());

            if (dto.getRolesAuth0Ids() != null) {
                Set<Rol> nuevosRoles = dto.getRolesAuth0Ids().stream()
                        .map(roleId -> repoRol.findByAuth0RoleId(roleId)
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado en BD: " + roleId)))
                        .collect(Collectors.toSet());
                usuarioExistente.setRol(nuevosRoles.iterator().next());
            }

            return (T) userBBDDService.save(usuarioExistente);

        } catch (Exception e) {
            throw new RuntimeException("Error al modificar usuario con Auth0 ID: " + auth0Id, e);
        }
    }

    /**
     * Alta o baja lógica del usuario (activo / inactivo) sincronizado con Auth0.
     */
    @Transactional
    public void cambiarEstadoUsuario(String auth0Id, boolean activo) {
        try {
            T usuario = (T) userBBDDService.findByIdAuth0(auth0Id);
            userBBDDService.save(usuario);
            userAuth0Service.blockUser(auth0Id, !activo);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar estado del usuario: " + auth0Id, e);
        }
    }
}

