package com.example.demo.Application.DTO.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NuevaDireccionDto {
    private String calle;
    private String numero;
    private String piso;    // Opcional
    private String dpto;    // Opcional
    private Long idDepartamento; // Obligatorio para una nueva dirección
}