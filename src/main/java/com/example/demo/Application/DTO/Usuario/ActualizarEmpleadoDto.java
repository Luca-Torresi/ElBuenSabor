package com.example.demo.Application.DTO.Usuario;

import com.example.demo.Domain.Enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ActualizarEmpleadoDto {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Rol rol;
    private DireccionDto direccion;
}
