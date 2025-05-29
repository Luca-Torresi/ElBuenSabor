package com.example.demo.Application.DTO.ArticuloManufacturado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Información de la cantidad de un insumo específico para la elaboración del artículo manufacturado
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ArticuloManufacturadoDetalleDto {
    private Long idArticuloInsumo;
    private double cantidad;
}
