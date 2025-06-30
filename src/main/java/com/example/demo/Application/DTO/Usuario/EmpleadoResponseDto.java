package com.example.demo.Application.DTO.Usuario;

// Importa Rol si realmente lo necesitas como un Enum,
// pero si los roles de la entidad son String, considera cambiar a List<String>.
// import com.example.demo.Domain.Enums.Rol; // <--- Considera si realmente lo necesitas

import com.example.demo.Application.DTO.Departamento.DepartamentoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List; // <--- Para manejar múltiples roles como Strings

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class EmpleadoResponseDto {
    private Long idUsuario;
    private String auth0Id;
    private String nombre;
    private String apellido;
    private String email;
    private List<String> roles;

    private Boolean activo;
    private String telefono;
    private LocalDate fechaBaja;

    private String imagen;

    private DireccionResponseDto direccion;

}