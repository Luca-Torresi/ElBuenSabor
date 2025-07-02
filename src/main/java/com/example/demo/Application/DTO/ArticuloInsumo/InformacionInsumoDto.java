package com.example.demo.Application.DTO.ArticuloInsumo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Envía y recibe la información necesaria de un artículo insumo para ser modificado
//Como también para mostrar sus detalles en el ABM
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class InformacionInsumoDto {
    private Long idArticuloInsumo;
    private String nombre;
    private double stockActual;
    private double stockMinimo;
    private double stockMaximo;
    private boolean dadoDeAlta;
    private Long idRubro;
    private String nombreRubro;
    private Long idUnidadDeMedida;
    private String unidadDeMedida;
}
