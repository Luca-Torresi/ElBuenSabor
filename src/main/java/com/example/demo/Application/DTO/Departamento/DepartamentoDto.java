// src/main/java/com/example/demo/Application/DTO/Departamento/DepartamentoDto.java
package com.example.demo.Application.DTO.Departamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartamentoDto {
    private Long id;
    private String nombre;
    private String nombreProvincia; // Este es el DTO de respuesta y sí incluye la provincia
}