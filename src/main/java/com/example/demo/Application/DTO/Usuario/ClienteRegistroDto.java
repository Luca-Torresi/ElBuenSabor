package com.example.demo.Application.DTO.Usuario;

import lombok.AllArgsConstructor; // Para un constructor con todos los argumentos
import lombok.Builder;             // Para usar el patrón Builder
import lombok.Data;                // Para getters, setters, toString, equals, hashCode
import lombok.NoArgsConstructor;   // Para un constructor sin argumentos

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteRegistroDto {

    private String auth0Id;

    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private String imagen;

    private String nickName;
}
