package com.example.demo.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class DetalleFactura {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleFactura;
    private int cantidad;
    private double subTotal;

    @ManyToOne
    @JoinColumn(name = "idFactura")
    private Factura factura;
    @ManyToOne
    @JoinColumn(name = "idArticulo")
    private Articulo articulo;
}
