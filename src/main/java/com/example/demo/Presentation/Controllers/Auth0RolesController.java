package com.example.demo.Presentation.Controllers;

import com.auth0.exception.Auth0Exception;
import com.example.demo.Application.DTO.Rol.Auth0RoleDto;
import com.example.demo.Domain.Service.Auth.Auth0RoleManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Si quieres proteger este endpoint
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth0/roles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permite acceso desde cualquier origen (ajustar en producción)
public class Auth0RolesController {

    private final Auth0RoleManagementService auth0RoleManagementService;

    @GetMapping
    // @PreAuthorize("hasAuthority('ADMINISTRADOR')") // Opcional: Proteger para que solo los admins puedan ver esto
    public ResponseEntity<?> getAllAuth0Roles() {
        try {
            List<Auth0RoleDto> roles = auth0RoleManagementService.getAllAuth0RolesExposed();
            return ResponseEntity.ok(roles);
        } catch (Auth0Exception e) {
            // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener roles de Auth0: " + e.getMessage());
        }
    }
}
