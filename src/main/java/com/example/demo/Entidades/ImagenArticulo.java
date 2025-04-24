package com.example.demo.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class ImagenArticulo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImagenArticulo;
    private String url;

    @OneToOne
    @JoinColumn(name = "idArticulo")
    private Articulo articulo;
}
