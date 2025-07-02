package com.example.demo.Application.DTO.Usuario;

// import com.example.demo.Domain.Enums.Rol; // Ya no necesario si no se usa directamente
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NuevoEmpleadoDto {
    private String email;
    private String password; // Para crear la cuenta en Auth0
    private String nombre;
    private String apellido;
    private String telefono;
    private String nickName; // Si lo usas en Auth0
    private List<String> rolesAuth0Ids; // IDs de los roles en Auth0
}