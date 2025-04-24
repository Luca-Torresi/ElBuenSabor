package com.example.demo.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class RubroInsumo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRubroInsumo;
    private String nombre;
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "idRubroPadre")
    private RubroInsumo rubroPadre;
}
