package com.example.demo.Entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Ingrediente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIngrediente;
    private double cantidad;

    @ManyToOne
    @JoinColumn(name = "idArticulo")
    @JsonBackReference
    private Articulo articulo;
    @ManyToOne
    @JoinColumn(name = "idInsumo")
    private Insumo insumo;
}
