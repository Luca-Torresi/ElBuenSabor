package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data @SuperBuilder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class ArticuloManufacturado extends Articulo {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idImagenManufacturado")
    private ImagenManufacturado imagenManufacturado;

    @OneToMany(mappedBy = "articuloManufacturado", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ArticuloManufacturadoDetalle> detalles;

    private String receta;
    private int tiempoDeCocina;
}
