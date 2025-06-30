package com.example.demo.Domain.Service.Auth;

import com.example.demo.Domain.Entities.Roles; // Importa tu nueva entidad Rol
import com.example.demo.Domain.Entities.Usuario; // Usa Usuario, no User
import com.example.demo.Application.DTO.Usuario.UsuarioDTO;
import com.example.demo.Domain.Repositories.RepoRoles; // Nuevo repositorio
import com.example.demo.Domain.Repositories.RepoUsuario;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserBBDDService {

    private final RepoUsuario userRepository;
    private final RepoRoles rolRepository; // Inyecta el nuevo repositorio

    public UserBBDDService(RepoUsuario userRepository, RepoRoles rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }

    public List<Roles> findAllRoles() {
        return rolRepository.findAll();
    }

    public List<Usuario> findAllUsers() {
        return userRepository.findAllUser();
    }

    public Usuario findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Usuario findByIdAuth0(String auth0Id) {
        return userRepository.findByIdAuth0(auth0Id).orElse(null);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        return userRepository.save(usuario);
    }

    @Transactional
    public Usuario update(Usuario usuario) {
        // Asegúrate de que el usuario exista antes de actualizar
        Usuario existingUser = userRepository.findById(usuario.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en BD"));
        // Actualiza los campos necesarios
        existingUser.setEmail(usuario.getEmail());
        existingUser.setNombre(usuario.getNombre());
        existingUser.setApellido(usuario.getApellido());
        existingUser.setTelefono(usuario.getTelefono());
        existingUser.setImagen(usuario.getImagen()); // O maneja la imagen por separado
        existingUser.setRoles(usuario.getRoles()); // Actualiza los roles

        return userRepository.save(existingUser);
    }

    @Transactional
    public void delete(String auth0Id) { // Borrado lógico
        Usuario usuario = userRepository.findByIdAuth0(auth0Id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para borrado lógico: " + auth0Id));
        // Asumiendo que tu entidad Usuario extiende de Base y tiene el campo 'deleted'
        // Si no, debes añadirlo a Usuario o a una clase intermedia
        // usuario.setDeleted(true); // Descomenta si tienes un campo 'deleted' en Usuario
        userRepository.save(usuario); // Guarda el cambio de estado
    }

    @Transactional
    public void deleteFisic(String auth0Id) { // Borrado físico
        Usuario usuario = userRepository.findByIdAuth0(auth0Id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado para borrado físico: " + auth0Id));
        userRepository.delete(usuario);
    }

    // Método para crear un Usuario en BBDD a partir de un DTO
    @Transactional
    public Usuario createUserFromDTO(UsuarioDTO userDTO, String auth0Id) {
        Set<Roles> rolesAsignados = new HashSet<>();
        if (userDTO.getRolesAuth0Ids() != null && !userDTO.getRolesAuth0Ids().isEmpty()) {
            rolesAsignados = userDTO.getRolesAuth0Ids().stream()
                    .map(auth0RoleId -> rolRepository.findByAuth0RoleId(auth0RoleId)
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
                .roles(rolesAsignados)
                .build();

        return userRepository.save(nuevoUsuario);
    }

    // Método para actualizar los roles de un usuario en la BD local
    @Transactional
    public Usuario updateRoles(String auth0Id, List<String> auth0RoleIds) {
        Usuario usuario = userRepository.findByIdAuth0(auth0Id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en BD para actualizar roles: " + auth0Id));

        Set<Roles> nuevosRoles = auth0RoleIds.stream()
                .map(idRol -> rolRepository.findByAuth0RoleId(idRol)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado en BD: " + idRol)))
                .collect(Collectors.toSet());

        usuario.setRoles(nuevosRoles); // Reemplaza los roles existentes
        // Si quieres añadir en lugar de reemplazar:
        // usuario.getRoles().addAll(nuevosRoles); // Añade los roles nuevos

        return userRepository.save(usuario);
    }
}
