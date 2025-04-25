package com.example.demo.Entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class ArticuloManufacturado {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArticulo;
    private String nombre;
    private String descripcion;
    private String receta;
    private boolean activo;
    private int tiempoDeCocina;

    @OneToMany(mappedBy = "articuloManufacturado")
    @JsonManagedReference
    private List<ArticuloManufacturadoDetalle> detalles;

    @ManyToOne
    @JoinColumn(name = "idCategoria")
    @JsonBackReference
    private Categoria categoria;
}
