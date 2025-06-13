package com.example.demo.Application.DTO.Promocion;

import com.example.demo.Application.DTO.Generic.ImagenModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

//Información necesaria para la creación de una nueva promoción
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PromocionDto {
    private String titulo;
    private String descripcion;
    private double descuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private ImagenModel imagenModel;
    private Long idArticuloManufacturado;
}
