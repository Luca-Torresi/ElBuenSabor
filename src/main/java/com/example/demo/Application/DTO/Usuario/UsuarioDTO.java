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

    // ID interno de tu base de datos (opcional para operaciones de actualización por ID interno)
    private Long id;

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

    // Nickname para Auth0 (opcional, si lo usas)
    private String nickName;

    // IDs de los roles de Auth0 (para asignar/modificar roles)
    private List<String> rolesAuth0Ids;

    // Puedes añadir otros campos relevantes para el perfil general del usuario si es necesario
    // private String direccion;
    // private String ciudad;
}