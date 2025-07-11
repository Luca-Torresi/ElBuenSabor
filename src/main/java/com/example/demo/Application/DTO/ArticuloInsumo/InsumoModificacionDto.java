package com.example.demo.Application.DTO.ArticuloInsumo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class InsumoModificacionDto {
    private String nombre;
    private double stockMinimo;
    private double stockMaximo;
    private boolean dadoDeAlta;
    private Long idRubroInsumo;
    private Long idUnidadDeMedida;
}
