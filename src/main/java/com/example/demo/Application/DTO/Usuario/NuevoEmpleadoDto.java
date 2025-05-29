package com.example.demo.Application.DTO.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Recibe los datos necesarios para la creación de un nuevo empleado
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NuevoEmpleadoDto {
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
}
