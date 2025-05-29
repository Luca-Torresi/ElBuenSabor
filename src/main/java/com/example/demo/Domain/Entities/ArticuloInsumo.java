package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class ArticuloInsumo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArticuloInsumo;
    private String nombre;
    private double stockActual;
    private double stockMinimo;
    private double stockMaximo;
    private LocalDate fechaBaja;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idImagenInsumo")
    private ImagenInsumo imagenInsumo;

    @ManyToOne
    @JoinColumn(name = "idRubroInsumo")
    @JsonBackReference
    private RubroInsumo rubroInsumo;

    @ManyToOne
    @JoinColumn(name = "idUnidadDeMedida")
    private UnidadDeMedida unidadDeMedida;
}
