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
public class Categoria {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;
    private String nombre;
    private double margenGanancia;
    private LocalDate fechaBaja;

    @OneToMany(mappedBy = "categoria")
    @JsonManagedReference
    private List<ArticuloManufacturado> listaManufacturados;

    @OneToOne
    @JoinColumn(name = "idCategoriaPadre")
    private Categoria categoriaPadre;
}
