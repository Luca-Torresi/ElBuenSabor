package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private double precioUnitario;
    private double subTotal;
    private String nombreArticulo;

    @ManyToOne
    @JoinColumn(name = "idFactura")
    @JsonBackReference
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "idArticulo")
    private Articulo articulo;
}
