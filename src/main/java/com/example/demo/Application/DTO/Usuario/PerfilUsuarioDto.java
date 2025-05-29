package com.example.demo.Application.DTO.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Envía los datos del perfil del usuario una vez que inicia sesión (tanto cliente como empleado)
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PerfilUsuarioDto {
    private String nombre;
    private String apellido;
    private String urlImagen;
}
