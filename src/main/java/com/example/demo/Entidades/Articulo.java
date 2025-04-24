package com.example.demo.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Articulo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idArticulo;
    private String nombre;
    private String descripcion;
    private String receta;
    private boolean esElaborado;
    private boolean activo;
    private int tiempoDeCocina;

    @OneToOne
    @JoinColumn(name = "idCategoria")
    private Categoria categoria;
}
