package com.example.demo.Application.DTO.Usuario;

import com.example.demo.Domain.Enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ActualizarEmpleadoDto {
    // No incluyas idEmpleado si el path variable ya lo envía
    private String auth0Id; // Necesario para buscar y actualizar en Auth0
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private String nickName; // Si lo usas en Auth0
    private List<String> rolesAuth0Ids; // Nuevos roles a asignar
    private DireccionDto direccion;
}
