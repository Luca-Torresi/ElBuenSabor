package com.example.demo.Application.DTO.Promocion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NuevaPromocionDto {
    private String titulo;
    private String descripcion;
    private Double precio;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private Boolean activo;
    private List<NuevoDetallePromocionDto> detalles;
}
