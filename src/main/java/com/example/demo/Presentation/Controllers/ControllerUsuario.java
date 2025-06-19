package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Rol.AssingRoleDto;
import com.example.demo.Application.DTO.Usuario.UsuarioDTO;
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
    private final RepoRoles rolRepository;

    // Endpoint para obtener todos los usuarios (solo ADMINISTRADOR)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/todos")
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<Usuario> users = userBBDDService.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener los usuarios: " + e.getMessage());
        }
    }

    // Endpoint para buscar un usuario por su ID de Auth0 (solo ADMINISTRADOR)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/por-auth0-id/{auth0Id}")
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

    // Endpoint para crear un usuario (ej., un nuevo empleado - solo ADMINISTRADOR)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDTO userDTO) {
        try {
            com.auth0.json.mgmt.users.User auth0UserResponse = userAuth0Service.createUser(userDTO);

            if (userDTO.getRolesAuth0Ids() != null && !userDTO.getRolesAuth0Ids().isEmpty()) {
                userAuth0Service.assignRoles(auth0UserResponse.getId(), userDTO.getRolesAuth0Ids());
            }

            Set<Roles> rolesAsignadosBD = new HashSet<>();
            if (userDTO.getRolesAuth0Ids() != null && !userDTO.getRolesAuth0Ids().isEmpty()) {
                rolesAsignadosBD = userDTO.getRolesAuth0Ids().stream()
                        .map(idRolAuth0 -> rolRepository.findByAuth0RoleId(idRolAuth0)
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado en BD: " + idRolAuth0)))
                        .collect(Collectors.toSet());
            }

            Usuario userBBDD = Usuario.builder()
                    .idAuth0(auth0UserResponse.getId())
                    .email(auth0UserResponse.getEmail())
                    .nombre(userDTO.getNombre() != null ? userDTO.getNombre() : auth0UserResponse.getName())
                    .apellido(userDTO.getApellido() != null ? userDTO.getApellido() : "")
                    .telefono(userDTO.getTelefono())
                    .roles(rolesAsignadosBD)
                    .build();

            return ResponseEntity.ok(userBBDDService.save(userBBDD));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear el usuario: " + e.getMessage());
        }
    }

    // Endpoint para modificar un usuario (solo ADMINISTRADOR)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PutMapping("/{auth0Id}/modificar")
    public ResponseEntity<?> modificarUsuario(@PathVariable String auth0Id, @RequestBody UsuarioDTO userDTO) {
        try {
            if (auth0Id == null || auth0Id.isEmpty()) {
                return ResponseEntity.badRequest().body("ID de Auth0 es requerido para modificar usuario.");
            }
            userDTO.setAuth0Id(auth0Id);

            com.auth0.json.mgmt.users.User auth0UserUpdated = userAuth0Service.modifyUser(userDTO);

            if (userDTO.getRolesAuth0Ids() != null) {
                userAuth0Service.assignRoles(auth0UserUpdated.getId(), userDTO.getRolesAuth0Ids());
            }

            Usuario userToUpdate = userBBDDService.findByIdAuth0(auth0Id);
            if(userToUpdate == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado en BD.");
            }

            userToUpdate.setEmail(userDTO.getEmail());
            userToUpdate.setNombre(userDTO.getNombre());
            userToUpdate.setApellido(userDTO.getApellido());
            userToUpdate.setTelefono(userDTO.getTelefono());

            Set<Roles> rolesAsignadosBD = new HashSet<>();
            if (userDTO.getRolesAuth0Ids() != null && !userDTO.getRolesAuth0Ids().isEmpty()) {
                rolesAsignadosBD = userDTO.getRolesAuth0Ids().stream()
                        .map(idRolAuth0 -> rolRepository.findByAuth0RoleId(idRolAuth0)
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado en BD: " + idRolAuth0)))
                        .collect(Collectors.toSet());
            }
            userToUpdate.setRoles(rolesAsignadosBD);

            return ResponseEntity.ok(userBBDDService.save(userToUpdate));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al modificar usuario: " + e.getMessage());
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

    // Endpoint para agregar roles a un usuario (solo ADMINISTRADOR)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping("/{auth0Id}/roles/agregar")
    public ResponseEntity<?> agregarRoles(@PathVariable String auth0Id, @RequestBody AssingRoleDto request) {
        try {
            userAuth0Service.assignRoles(auth0Id, request.getRoles());

            Usuario user = userBBDDService.findByIdAuth0(auth0Id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado en BD.");
            }

            Set<Roles> rolesAAgregar = request.getRoles().stream()
                    .map(idRolAuth0 -> rolRepository.findByAuth0RoleId(idRolAuth0)
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado en BD: " + idRolAuth0)))
                    .collect(Collectors.toSet());

            user.getRoles().addAll(rolesAAgregar);
            return ResponseEntity.ok(userBBDDService.save(user));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al agregar roles: " + e.getMessage());
        }
    }

    // Endpoint para quitar roles a un usuario (solo ADMINISTRADOR)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping("/{auth0Id}/roles/quitar")
    public ResponseEntity<?> quitarRoles(@PathVariable String auth0Id, @RequestBody AssingRoleDto request) {
        try {
            userAuth0Service.removeRoles(auth0Id, request.getRoles());

            Usuario user = userBBDDService.findByIdAuth0(auth0Id);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado en BD.");
            }

            Set<Roles> rolesAEliminar = request.getRoles().stream()
                    .map(idRolAuth0 -> rolRepository.findByAuth0RoleId(idRolAuth0)
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado en BD: " + idRolAuth0)))
                    .collect(Collectors.toSet());

            user.getRoles().removeAll(rolesAEliminar);
            return ResponseEntity.ok(userBBDDService.save(user));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al quitar roles: " + e.getMessage());
        }
    }

    // Endpoint para el auto-registro de un nuevo cliente (ACCESO PÚBLICO)
    // No requiere @PreAuthorize porque es el punto de entrada para nuevos usuarios/clientes.
    @PostMapping("/registrar-cliente")
    public ResponseEntity<?> registrarCliente(@RequestBody UsuarioDTO userDTO) {
        try {
            Usuario existingUserBBDD = userBBDDService.findByIdAuth0(userDTO.getAuth0Id());
            if (existingUserBBDD != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El usuario ya existe en la base de datos.");
            }

            com.auth0.json.mgmt.users.User auth0User = userAuth0Service.getUserById(userDTO.getAuth0Id());
            if(auth0User == null) {
                return ResponseEntity.internalServerError().body("El usuario no existe en Auth0.");
            }

            Roles clienteRol = rolRepository.findByName("CLIENTE")
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado en BD."));
            List<String> rolesAuth0Ids = List.of(clienteRol.getAuth0RoleId());

            userAuth0Service.assignRoles(auth0User.getId(), rolesAuth0Ids);

            Set<Roles> rolesParaBD = new HashSet<>();
            rolesParaBD.add(clienteRol);

            Usuario userBBDD = Usuario.builder()
                    .idAuth0(auth0User.getId())
                    .email(auth0User.getEmail() != null ? auth0User.getEmail() : userDTO.getEmail())
                    .nombre(auth0User.getName() != null ? auth0User.getName() : userDTO.getNombre())
                    .apellido(userDTO.getApellido())
                    .telefono(userDTO.getTelefono())
                    .roles(rolesParaBD)
                    .build();

            return ResponseEntity.ok(userBBDDService.save(userBBDD));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al registrar nuevo cliente: " + e.getMessage());
        }
    }
}