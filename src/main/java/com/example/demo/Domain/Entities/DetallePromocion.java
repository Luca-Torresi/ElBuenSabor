package com.example.demo.Domain.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class DetallePromocion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetallePromocion;

    @ManyToOne @JoinColumn(name = "idArticulo")
    private Articulo articulo;
    private Integer cantidad;

    @ManyToOne @JoinColumn(name = "idPromocion")
    private Promocion promocion;
}
