package com.example.demo.Domain.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data @SuperBuilder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class ArticuloNoElaborado extends Articulo {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idImagenNoElaborado")
    private ImagenNoElaborado imagen;

    private double costo;
}
