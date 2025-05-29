package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class ArticuloManufacturado {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArticuloManufacturado;
    private String nombre;
    private String descripcion;
    private double precioVenta;
    private String receta;
    private int tiempoDeCocina;
    private LocalDate fechaBaja;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idImagenManufacturado")
    private ImagenManufacturado imagenManufacturado;

    @ManyToOne
    @JoinColumn(name = "idCategoria")
    @JsonBackReference
    private Categoria categoria;

    @OneToMany(mappedBy = "articuloManufacturado", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ArticuloManufacturadoDetalle> detalles;
}
