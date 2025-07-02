package com.example.demo.Application.DTO.Usuario;

import lombok.AllArgsConstructor; // Para un constructor con todos los argumentos
import lombok.Builder;             // Para usar el patrón Builder
import lombok.Data;                // Para getters, setters, toString, equals, hashCode
import lombok.NoArgsConstructor;   // Para un constructor sin argumentos

import java.util.List;

@Data // Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
@Builder // Permite construir objetos con el patrón Builder
public class UsuarioDTO {

    // ID de Auth0: CRUCIAL para identificar el usuario en Auth0
    private String auth0Id;

    // Campos del perfil del usuario
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private String imagen; // Si manejas la URL de la imagen

    // Para la creación de usuarios en Auth0 que requieren una contraseña inicial
    // NOTA: Esta contraseña NUNCA debe ser devuelta en una respuesta.
    private String password;

    // IDs de los roles de Auth0 (para asignar/modificar roles)
    private List<String> rolesAuth0Ids;

}