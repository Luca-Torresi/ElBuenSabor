package com.example.demo.Domain.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class ImagenManufacturado {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImagenManufacturado;
    private String url;
}
