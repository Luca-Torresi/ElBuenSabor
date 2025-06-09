package com.example.demo.Domain.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data @SuperBuilder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class ArticuloNoElaborado extends Articulo {

    private double costo;
    private int stock;
}
