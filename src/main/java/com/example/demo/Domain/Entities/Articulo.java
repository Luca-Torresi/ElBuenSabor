package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.List;

@Data @SuperBuilder @AllArgsConstructor @NoArgsConstructor
@Entity @Table @Inheritance(strategy = InheritanceType.JOINED)
public class Articulo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArticulo;
    private String nombre;
    private String descripcion;
    private Double precioVenta;
    private LocalDate fechaBaja;
    private Boolean esManufacturado;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idImagen")
    private Imagen imagen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCategoria")
    @JsonBackReference
    private Categoria categoria;

    @OneToMany(mappedBy = "articulo", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    @JsonIgnore
    private List<DetallePromocion> detallesPromocion;
}
