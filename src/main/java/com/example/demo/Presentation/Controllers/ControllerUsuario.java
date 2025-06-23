package com.example.demo.Presentation.Controllers;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.example.demo.Application.DTO.Rol.AssingRoleDto;
import com.example.demo.Application.DTO.Rol.Auth0RoleDto;
import com.example.demo.Application.DTO.Rol.RoleDto;
import com.example.demo.Application.DTO.Usuario.Auth0UsuarioDto;
import com.example.demo.Application.DTO.Usuario.UsuarioDTO;
import com.example.demo.Application.DTO.Usuario.UsuarioRespDto;
import com.example.demo.Domain.Entities.Roles;
import com.example.demo.Domain.Entities.Usuario;
import com.example.demo.Domain.Repositories.RepoRoles;
import com.example.demo.Domain.Service.Auth.UserAuth0Service;
import com.example.demo.Domain.Service.Auth.UserBBDDService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
public class ControllerUsuario {

    private final UserAuth0Service userAuth0Service;
    private final UserBBDDService userBBDDService;

    // Endpoint para obtener todos los usuarios (solo ADMINISTRADOR)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/todos")
    @Transactional(readOnly = true)  // Añadir esta anotación
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Usuario> usuarios = userBBDDService.findAllUsers();

            List<UsuarioRespDto> response = usuarios.stream()
                    .map(usuario -> {
                        UsuarioRespDto dto = new UsuarioRespDto();
                        dto.setIdUsuario(usuario.getIdUsuario());
                        dto.setIdAuth0(usuario.getIdAuth0());
                        dto.setEmail(usuario.getEmail());
                        dto.setNombre(usuario.getNombre());
                        dto.setApellido(usuario.getApellido());
                        dto.setTelefono(usuario.getTelefono());
                        dto.setActivo(usuario.getActivo());

                        // Convertir a un nuevo Set para evitar problemas de lazy loading
                        Set<String> roles = new HashSet<>(usuario.getRoles().stream()
                                .map(Roles::getName)
                                .collect(Collectors.toSet()));
                        dto.setRoles(roles);

                        return dto;
                    }).toList();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error al obtener los usuarios: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
        }
    }



    @GetMapping("/todosRoles")
    // @PreAuthorize("hasAuthority('ADMINISTRADOR')") // Opcional: Proteger para que solo los admins puedan ver esto
    public ResponseEntity<?> getAllAuth0Roles() {
        try {
            List<Auth0RoleDto> roles = userAuth0Service.getAllRole();
            List<Roles> roleBD = userBBDDService.findAllRoles();
            return ResponseEntity.ok(roleBD);
        } catch (Auth0Exception e) {
            // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener roles de Auth0: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/auth0/todos")
    public ResponseEntity<?> obtenerTodosUsuariosAuth0() {
        try {
            List<User> auth0Users = userAuth0Service.getAllUsers();

            List<Auth0UsuarioDto> usuariosDto = auth0Users.stream()
                    .map(user -> new Auth0UsuarioDto(
                            user.getId(),
                            user.getEmail(),
                            user.getName(),
                            user.isBlocked() != null ? user.isBlocked() : false
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(usuariosDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener usuarios de Auth0: " + e.getMessage());
        }
    }


    // Endpoint para buscar un usuario por su ID de Auth0 (solo ADMINISTRADOR)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/{auth0Id}")
    public ResponseEntity<?> buscarPorAuth0Id(@PathVariable String auth0Id) {
        try {
            Usuario user = userBBDDService.findByIdAuth0(auth0Id);
            if(user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al buscar usuario: " + e.getMessage());
        }
    }

    // Endpoint para dar de baja lógica a un usuario (solo ADMINISTRADOR)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @DeleteMapping("/{auth0Id}/baja-logica")
    public ResponseEntity<?> bajaLogicaUsuario(@PathVariable String auth0Id) {
        try {
            userBBDDService.delete(auth0Id);
            // Aquí puedes llamar a Auth0 para deshabilitar el usuario también si es necesario
            return ResponseEntity.ok("Usuario " + auth0Id + " dado de baja (lógica) correctamente.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al dar de baja (lógica) al usuario: " + e.getMessage());
        }
    }

    // Endpoint para eliminar físicamente un usuario (solo ADMINISTRADOR)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @DeleteMapping("/{auth0Id}/baja-fisica")
    public ResponseEntity<?> bajaFisicaUsuario(@PathVariable String auth0Id) {
        try {
            userBBDDService.deleteFisic(auth0Id);
            userAuth0Service.deleteUser(auth0Id);
            return ResponseEntity.ok("Usuario " + auth0Id + " eliminado físicamente.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar físicamente al usuario: " + e.getMessage());
        }
    }
}