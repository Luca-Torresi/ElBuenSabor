package com.example.demo.Application.DTO.ArticuloManufacturado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class InformacionDetalleDto {
    private Long idArticuloInsumo;
    private String nombreInsumo;
    private String unidadDeMedida;
    private double cantidad;
}
