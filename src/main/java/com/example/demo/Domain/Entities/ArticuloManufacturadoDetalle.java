package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class ArticuloManufacturadoDetalle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArticuloManufacturadoDetalle;
    private double cantidad;

    @ManyToOne
    @JoinColumn(name = "idArticuloManufacturado")
    @JsonBackReference
    private ArticuloManufacturado articuloManufacturado;

    @ManyToOne
    @JoinColumn(name = "idArticuloInsumo")
    private ArticuloInsumo articuloInsumo;
}
