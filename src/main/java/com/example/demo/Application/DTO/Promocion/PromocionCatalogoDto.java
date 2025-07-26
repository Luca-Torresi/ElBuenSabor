package com.example.demo.Application.DTO.Promocion;

import com.example.demo.Application.DTO.Generic.ImagenModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PromocionCatalogoDto {
    private Long idPromocion;
    private String titulo;
    private String descripcion;
    private Long idArticulo;
    private double descuento;
    private String url;
}
