package com.example.demo.Application.DTO.Usuario;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class EmpleadoResponseDto {
    private Long idUsuario;
    private String auth0Id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private String telefono;
    private LocalDate fechaBaja;

    private String imagen;
}