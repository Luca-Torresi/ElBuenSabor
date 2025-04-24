package com.example.demo.Entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Insumo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIsumo;
    private String nombre;
    private double stockActual;
    private double stockMinimo;
    private double stockMaximo;
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "idUnidadDeMedida")
    private UnidadDeMedida unidadDeMedida;
    @ManyToOne
    @JoinColumn(name = "idRubroInsumo")
    @JsonBackReference
    private RubroInsumo rubroInsumo;
}
