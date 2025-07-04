package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class RubroInsumo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRubroInsumo;
    private String nombre;
    private LocalDate fechaBaja;

    @OneToMany(mappedBy = "rubroInsumo")
    @JsonManagedReference
    private List<ArticuloInsumo> insumos;

    @ManyToOne
    @JoinColumn(name = "idRubroIsumoPadre")
    private RubroInsumo rubroInsumoPadre;
}
