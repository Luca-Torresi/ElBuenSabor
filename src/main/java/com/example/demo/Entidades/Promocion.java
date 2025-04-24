package com.example.demo.Entidades;

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
    private int idPromocion;
    private String nombre;
    private String titulo;
    private String descripcion;
    private String descuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String url;

    @ManyToOne
    @JoinColumn(name = "idArticulo")
    private Articulo articulo;
}
