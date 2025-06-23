package com.example.demo.Application.DTO.Usuario;

import lombok.Data;

import java.util.Set;

@Data
public class UsuarioRespDto {
    private Long idUsuario;
    private String idAuth0;
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private Boolean activo;
    private Set<String> roles;
}

