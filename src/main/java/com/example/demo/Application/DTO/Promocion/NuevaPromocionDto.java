package com.example.demo.Application.DTO.Promocion;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NuevaPromocionDto {
    private String titulo;
    private String descripcion;
    private Double precioPromocional;
    private String horarioInicio;
    private String horarioFin;
    private Boolean activo;
    private List<DetallePromocionDto> detalles;
    @Getter
    private boolean eliminarImagen;
}