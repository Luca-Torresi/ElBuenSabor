package com.example.demo.Domain.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Promocion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPromocion;
    private String titulo;
    private String descripcion;
    private double descuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String url;

    @ManyToOne
    @JoinColumn(name = "idArticuloManufacturado")
    private ArticuloManufacturado articuloManufacturado;
}
