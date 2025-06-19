package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Usuario.UsuarioDTO;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Service.ServiceCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional; // Importar Optional

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/cliente", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class ControllerCliente {

    private final ServiceCliente serviceCliente;

    private String getAuth0IdFromAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getSubject();
        }
        throw new IllegalStateException("Usuario no autenticado o ID de Auth0 no disponible.");
    }

    // --- Endpoints para el Cliente Autenticado ---

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerMiPerfil() {
        try {
            String auth0Id = getAuth0IdFromAuthenticatedUser();
            Optional<Cliente> clienteOptional = serviceCliente.obtenerMiPerfil(auth0Id);

            if (clienteOptional.isPresent()) {
                return ResponseEntity.ok().body(clienteOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil de cliente no encontrado.");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener el perfil: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PutMapping("/perfil")
    public ResponseEntity<Cliente> actualizarMiPerfil(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            String auth0Id = getAuth0IdFromAuthenticatedUser();
            Cliente clienteActualizado = serviceCliente.actualizarMiPerfil(auth0Id, usuarioDTO);
            return ResponseEntity.ok(clienteActualizado);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- Endpoints para el Registro de Clientes (Público, para crear cuenta inicial) ---

    @PostMapping("/registrar")
    public ResponseEntity<Cliente> registrarCliente(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            if (usuarioDTO.getAuth0Id() == null || usuarioDTO.getAuth0Id().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Cliente nuevoCliente = serviceCliente.registrarNuevoCliente(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- Endpoints para la Gestión de Clientes por un Administrador ---

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/todos-activos")
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientesActivos() {
        List<Cliente> clientes = serviceCliente.obtenerTodosLosClientesActivos();
        return ResponseEntity.ok(clientes);
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerClientePorId(@PathVariable Long id) {
        Optional<Cliente> clienteOptional = serviceCliente.obtenerClientePorId(id);

        if (clienteOptional.isPresent()) {
            return ResponseEntity.ok().body(clienteOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado.");
        }
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PutMapping("/alta-baja-logica/{idCliente}")
    public ResponseEntity<String> altaBajaLogicaCliente(@PathVariable Long idCliente) {
        try {
            serviceCliente.altaBajaLogicaCliente(idCliente);
            return ResponseEntity.ok("Estado del cliente actualizado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar estado del cliente: " + e.getMessage());
        }
    }
}