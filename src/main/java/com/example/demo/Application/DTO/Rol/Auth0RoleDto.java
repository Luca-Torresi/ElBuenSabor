package com.example.demo.Application.DTO.Rol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auth0RoleDto {
    private String id;   // El ID de Auth0 del rol (ej. rol_abcdef...)
    private String name; // El nombre del rol (ej. "ADMINISTRADOR")
}
