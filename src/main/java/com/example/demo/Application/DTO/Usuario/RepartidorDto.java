package com.example.demo.Application.DTO.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepartidorDto {
    private Long idUsuario;
    private String auth0Id;
    private String nombre;
    private String apellido;
    private String telefono;
}
