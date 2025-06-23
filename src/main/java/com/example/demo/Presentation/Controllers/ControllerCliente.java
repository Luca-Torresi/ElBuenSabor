package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Usuario.ClienteDto;
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

    @PreAuthorize("hasAuthority('CLIENTE')")
    @GetMapping("/perfil")
    public ResponseEntity<ClienteDto> obtenerMiPerfil() {
        String auth0Id = getAuth0IdFromAuthenticatedUser();
        return serviceCliente.obtenerMiPerfil(auth0Id)
                .map(cliente -> {
                    ClienteDto dto = new ClienteDto();
                    dto.setAuth0Id(cliente.getIdAuth0());
                    dto.setNombre(cliente.getNombre());
                    dto.setApellido(cliente.getApellido());
                    dto.setEmail(cliente.getEmail());
                    dto.setTelefono(cliente.getTelefono());
                    dto.setImagen(cliente.getImagen().getUrl());
                    return ResponseEntity.ok(dto);
                })
                .orElseThrow(() -> new RuntimeException("Perfil de cliente no encontrado."));
    }

    @PreAuthorize("hasAuthority('CLIENTE')")
    @PutMapping("/perfil")
    public ResponseEntity<Cliente> actualizarMiPerfil(@RequestBody UsuarioDTO usuarioDTO) {
        String auth0Id = getAuth0IdFromAuthenticatedUser();
        Cliente clienteActualizado = serviceCliente.actualizarMiPerfil(auth0Id, usuarioDTO);
        return ResponseEntity.ok(clienteActualizado);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Cliente> registrarCliente(@RequestBody ClienteDto clienteDto) {
        if (clienteDto.getAuth0Id() == null || clienteDto.getAuth0Id().isEmpty()) {
            throw new IllegalArgumentException("El campo auth0Id es obligatorio para registrar un cliente.");
        }
        Cliente nuevoCliente = serviceCliente.registrarNuevoCliente(clienteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/todos-activos")
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientesActivos() {
        return ResponseEntity.ok(serviceCliente.obtenerTodosLosClientesActivos());
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        return serviceCliente.obtenerClientePorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PutMapping("/alta-baja-logica/{idCliente}")
    public ResponseEntity<String> altaBajaLogicaCliente(@PathVariable Long idCliente) {
        serviceCliente.altaBajaLogicaCliente(idCliente);
        return ResponseEntity.ok("Estado del cliente actualizado correctamente.");
    }
}
