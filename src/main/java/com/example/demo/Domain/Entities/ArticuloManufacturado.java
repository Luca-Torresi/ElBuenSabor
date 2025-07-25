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
    private String receta;
    private int tiempoDeCocina;

    @JsonManagedReference
    @OneToMany(mappedBy = "articuloManufacturado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticuloManufacturadoDetalle> detalles;
}
