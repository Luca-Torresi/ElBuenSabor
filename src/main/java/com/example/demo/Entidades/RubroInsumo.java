package com.example.demo.Entidades;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class RubroInsumo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRubroInsumo;
    private String nombre;
    private boolean activo;

    @OneToMany(mappedBy = "rubroInsumo")
    @JsonManagedReference
    private List<Insumo> insumos;

    @ManyToOne
    @JoinColumn(name = "idRubroPadre")
    private RubroInsumo rubroPadre;
}
