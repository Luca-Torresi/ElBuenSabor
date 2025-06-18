package com.example.demo.Application.DTO.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para la dirección del empleado
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DireccionDto {
    private Long idDireccion;
    private String calle;
    private String numero;
    private String piso;    // Opcional
    private String dpto;    // Opcional
    private Long idDepartamento;
}
