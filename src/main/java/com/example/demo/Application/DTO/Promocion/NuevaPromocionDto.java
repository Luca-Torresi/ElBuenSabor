package com.example.demo.Application.DTO.Promocion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NuevaPromocionDto {
    private String titulo;
    private String descripcion;
    private double descuento;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private boolean activo;
    private Long idArticulo;
}
