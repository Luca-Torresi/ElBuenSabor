package com.example.demo.Application.DTO.Promocion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PromocionAbmDto {
    private Long idPromocion;
    private String titulo;
    private String descripcion;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private boolean activo;
    private String url;
    private List<DetallePromocionDto> detalles;
}
