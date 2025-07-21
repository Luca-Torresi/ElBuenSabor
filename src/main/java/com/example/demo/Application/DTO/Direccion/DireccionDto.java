package com.example.demo.Application.DTO.Direccion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class DireccionDto {
    private Long idDireccion;
    private String nombre;
    private String calle;
    private String numero;
    private String piso;
    private String dpto;
    private String nombreDepartamento;
    private Long idDepartamento;
}
