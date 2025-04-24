package com.example.demo.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Ingrediente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idIngrediente;
    private double cantidad;

    @ManyToOne
    @JoinColumn(name = "idArticulo")
    private Articulo articulo;
    @ManyToOne
    @JoinColumn(name = "idInsumo")
    private Insumo insumo;
}
