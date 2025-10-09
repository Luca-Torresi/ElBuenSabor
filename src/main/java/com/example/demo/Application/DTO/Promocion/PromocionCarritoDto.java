package com.example.demo.Application.DTO.Promocion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromocionCarritoDto {
    private Long idPromocion;
    private String titulo;
    private String descripcion;
    private String url;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private List<DetallePromocionDto> detalles;
    private Double precioBase;
    private Double precioPromocion;
    private Double ahorro;
}
