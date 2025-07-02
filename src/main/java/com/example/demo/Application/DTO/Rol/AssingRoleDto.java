package com.example.demo.Application.DTO.Rol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AssingRoleDto {
    private String id; // El ID de Auth0 del usuario
    private List<String> roles; // IDs de roles de Auth0
}
