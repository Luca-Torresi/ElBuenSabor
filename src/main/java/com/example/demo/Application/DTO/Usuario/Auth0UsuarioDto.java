package com.example.demo.Application.DTO.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth0UsuarioDto {

    private String id;          // ID de Auth0 (user_id)
    private String email;       // Email del usuario
    private String nombre;      // Nombre completo (name)
    private Boolean bloqueado;  // Estado de bloqueo en Auth0
}

