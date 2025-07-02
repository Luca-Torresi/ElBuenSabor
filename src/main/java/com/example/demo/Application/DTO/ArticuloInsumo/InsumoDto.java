package com.example.demo.Application.DTO.ArticuloInsumo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class InsumoDto {
    private Long idArticuloInsumo;
    private String unidadDeMedida;
    private String nombre;
    private double costo;
}
