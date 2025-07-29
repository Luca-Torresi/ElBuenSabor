package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Usuario.*;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Service.ServiceCliente;
import com.example.demo.Domain.Service.ServiceImagen;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional; // Importar Optional

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/cliente", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class ControllerCliente {
    private final ServiceCliente serviceCliente;
    private final ServiceImagen serviceImagen;

    private String getAuth0IdFromAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getSubject();
        }
        throw new IllegalStateException("Usuario no autenticado o ID de Auth0 no disponible.");
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping("/perfil")
    public ResponseEntity<ClienteDto> obtenerMiPerfil() {
        String auth0Id = getAuth0IdFromAuthenticatedUser();
        return serviceCliente.obtenerMiPerfil(auth0Id)
                .map(cliente -> {
                    ClienteDto dto = new ClienteDto();
                    dto.setIdUsuario(cliente.getIdUsuario());
                    dto.setAuth0Id(cliente.getIdAuth0());
                    dto.setNombre(cliente.getNombre());
                    dto.setApellido(cliente.getApellido());
                    dto.setEmail(cliente.getEmail());
                    dto.setTelefono(cliente.getTelefono());
                    dto.setImagen(cliente.getImagen() != null ? cliente.getImagen().getUrl() : null);
                    return ResponseEntity.ok(dto);
                })
                .orElseThrow(() -> new RuntimeException("Perfil de cliente no encontrado."));
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PutMapping("/actualizar/telefono")
    public ResponseEntity<Cliente> actualizarTelefono(@Valid @RequestBody TelefonoUpdateDto telefonoDto) {
        String auth0Id = getAuth0IdFromAuthenticatedUser();
        Cliente clienteActualizado = serviceCliente.actualizarTelefono(auth0Id, telefonoDto.getTelefono());
        return ResponseEntity.ok(clienteActualizado);
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PutMapping("/actualizar/contrasena") // Un nuevo endpoint para el cambio directo
    public ResponseEntity<String> actualizarContrasenaDirectamente(@RequestBody PasswordChangeDto passwordChangeDto) {
        String auth0Id = getAuth0IdFromAuthenticatedUser();
        try {
            // Aquí, si necesitas verificar la contraseña actual del usuario,
            // NO LO HAGAS EN TU BACKEND. Deberías usar las reglas/Actions de Auth0
            // o un flujo de autenticación paso a paso si es necesario.
            // Este endpoint asume que el frontend tiene un "derecho" o que la seguridad ya se manejó.

            serviceCliente.actualizarContrasenaDirectamente(auth0Id, passwordChangeDto);
            return ResponseEntity.ok("{\"message\": \"Contraseña actualizada exitosamente.\" }");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (RuntimeException e) { // Captura la RuntimeException lanzada por el servicio
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PutMapping("/perfil")
    public ResponseEntity<Cliente> actualizarMiPerfil(@RequestBody UsuarioDTO usuarioDTO) {
        String auth0Id = getAuth0IdFromAuthenticatedUser();
        Cliente clienteActualizado = serviceCliente.actualizarMiPerfil(auth0Id, usuarioDTO);
        return ResponseEntity.ok(clienteActualizado);
    }

    @PreAuthorize("hasAuthority('CLIENTE')") // Solo un cliente autenticado puede subir su imagen
    @PostMapping(value = "/{id}/imagen/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // Endpoint claro y RESTful
    public ResponseEntity<Map<String, String>> uploadProfileImage(
            @PathVariable("id") Long idCliente, // El ID de la URL
            @RequestParam("file") MultipartFile file) { // El archivo de imagen
        try {
            // No necesitas el Auth0Id aquí directamente, tu ServiceImagen ya lo maneja por el ID del cliente
            return serviceImagen.uploadProfileImage(file, idCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "ERROR", "message", e.getMessage()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "ERROR", "message", "Error al subir la imagen de perfil: " + e.getMessage()));
        }
    }

    @PreAuthorize("hasAuthority('CLIENTE')") // Solo un cliente autenticado puede eliminar su imagen
    @DeleteMapping("/imagen/delete") // Un DELETE sin ID en la URL, ya que se asocia al usuario logueado
    public ResponseEntity<String> deleteProfileImage() {
        try {
            // Primero obtenemos el ID de Auth0 del usuario autenticado
            String auth0Id = getAuth0IdFromAuthenticatedUser();

            // Luego, obtenemos el ID de la base de datos a partir del Auth0Id para pasarlo al servicio de imagen
            // Esto es crucial porque serviceImagen.deleteProfileImage espera un Long idUsuario
            Optional<Cliente> clienteOptional = serviceCliente.obtenerMiPerfil(auth0Id);
            if (clienteOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"status\":\"ERROR\", \"message\":\"Perfil de cliente no encontrado para eliminar imagen.\"}");
            }
            Long idCliente = clienteOptional.get().getIdUsuario();

            return serviceImagen.deleteProfileImage(idCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"status\":\"ERROR\", \"message\":\"Error al eliminar la imagen de perfil: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<Cliente> registrarCliente(@RequestBody ClienteRegistroDto clienteDto) {
        if (clienteDto.getAuth0Id() == null || clienteDto.getAuth0Id().isEmpty()) {
            throw new IllegalArgumentException("El campo auth0Id es obligatorio para registrar un cliente.");
        }
        Cliente nuevoCliente = serviceCliente.registrarNuevoCliente(clienteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/todos-activos")
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientesActivos() {
        return ResponseEntity.ok(serviceCliente.obtenerTodosLosClientes());
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        return serviceCliente.obtenerClientePorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));
    }

    @GetMapping("/lista")
    public Page<InformacionClienteDto> obtenerListaCLientes(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "9") int size){
        return serviceCliente.obtenerlistaClientes(page,size)   ;
    }

}
