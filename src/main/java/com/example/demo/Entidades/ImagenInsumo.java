package com.example.demo.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class ImagenInsumo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idImagenInsumo;
    private String url;

    @OneToOne
    @JoinColumn(name = "idInsumo")
    private Insumo insumo;
}
