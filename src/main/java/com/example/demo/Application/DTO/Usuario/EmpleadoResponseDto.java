package com.example.demo.Application.DTO.Usuario;

import com.example.demo.Domain.Enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class EmpleadoResponseDto {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private Rol rol;
    private boolean activo; // true si fechaBaja == null
    private String telefono;

    private String calle;
    private String numero;
    private String piso;
    private String dpto;
    private String departamentoNombre; // Sacado de direccion.departamento.nombre

    public Long getId() {
        return idUsuario;
    }
}
