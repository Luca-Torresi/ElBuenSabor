package com.example.demo.Domain.Service.Auth;

import com.example.demo.Domain.Entities.Rol; // Importa tu nueva entidad Rol
import com.example.demo.Domain.Entities.Usuario; // Usa Usuario, no User
import com.example.demo.Application.DTO.Usuario.UsuarioDTO;
import com.example.demo.Domain.Repositories.RepoRol; // Nuevo repositorio
import com.example.demo.Domain.Repositories.RepoUsuario;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBBDDService {

    private final RepoUsuario repoUsuario;
    private final RepoRol repoRol;

    public List<Rol> findAllRoles() {
        return repoRol.findAll();
    }

    public List<Usuario> findAllUsers() {
        return repoUsuario.findAllUser();
    }

    public Usuario findById(Long id) {
        return repoUsuario.findById(id).orElse(null);
    }

    public Usuario findByIdAuth0(String auth0Id) {
        return repoUsuario.findByIdAuth0(auth0Id).orElse(null);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        return repoUsuario.save(usuario);
    }

    @Transactional
    public Usuario update(Usuario usuario) {
        // Asegúrate de que el usuario exista antes de actualizar
        Usuario existingUser = repoUsuario.findById(usuario.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en BD"));
        // Actualiza los campos necesarios
        existingUser.setEmail(usuario.getEmail());
        existingUser.setNombre(usuario.getNombre());
        existingUser.setApellido(usuario.getApellido());
        existingUser.setTelefono(usuario.getTelefono());
        existingUser.setImagen(usuario.getImagen()); // O maneja la imagen por separado
        existingUser.setRol(usuario.getRol()); // Actualiza los roles

        return repoUsuario.save(existingUser);
    }

    @Transactional
    public void delete(String auth0Id) { // Borrado lógico
        Usuario usuario = repoUsuario.findByIdAuth0(auth0Id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para borrado lógico: " + auth0Id));
        // Asumiendo que tu entidad Usuario extiende de Base y tiene el campo 'deleted'
        // Si no, debes añadirlo a Usuario o a una clase intermedia
        // usuario.setDeleted(true); // Descomenta si tienes un campo 'deleted' en Usuario
        repoUsuario.save(usuario); // Guarda el cambio de estado
    }

    @Transactional
    public void deleteFisic(String auth0Id) { // Borrado físico
        Usuario usuario = repoUsuario.findByIdAuth0(auth0Id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para borrado físico: " + auth0Id));
        repoUsuario.delete(usuario);
    }

    // Método para crear un Usuario en BBDD a partir de un DTO
    @Transactional
    public Usuario createUserFromDTO(UsuarioDTO userDTO, String auth0Id) {
        Set<Rol> rolAsignados = new HashSet<>();
        if (userDTO.getRolesAuth0Ids() != null && !userDTO.getRolesAuth0Ids().isEmpty()) {
            rolAsignados = userDTO.getRolesAuth0Ids().stream()
                    .map(auth0RoleId -> repoRol.findByAuth0RoleId(auth0RoleId)
                            .orElseThrow(() -> new RuntimeException("Rol de Auth0 no encontrado en BD: " + auth0RoleId)))
                    .collect(Collectors.toSet());
        }

        Usuario nuevoUsuario = Usuario.builder()
                .idAuth0(auth0Id) // Usamos el ID real de Auth0
                .email(userDTO.getEmail())
                .nombre(userDTO.getNombre())
                .apellido(userDTO.getApellido())
                .telefono(userDTO.getTelefono())
                // .imagen(userDTO.getImagen()) // Si manejas la imagen aquí
                .rol(rolAsignados.iterator().next())
                .build();

        return repoUsuario.save(nuevoUsuario);
    }

    // Método para actualizar el rol de un usuario en la BD local
    @Transactional
    public Usuario updateRol(String auth0Id, List<String> auth0RoleIds) {
        Usuario usuario = repoUsuario.findByIdAuth0(auth0Id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en BD para actualizar roles: " + auth0Id));

        Set<Rol> nuevosRoles = auth0RoleIds.stream()
                .map(idRol -> repoRol.findByAuth0RoleId(idRol)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado en BD: " + idRol)))
                .collect(Collectors.toSet());

        usuario.setRol(nuevosRoles.iterator().next()); // Reemplaza los roles existentes
        // Si quieres añadir en lugar de reemplazar:
        // usuario.getRoles().addAll(nuevosRoles); // Añade los roles nuevos

        return repoUsuario.save(usuario);
    }
}
