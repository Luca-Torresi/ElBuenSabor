package com.example.demo.Domain.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "imagen")
public class Imagen {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "public_id", unique = true, nullable = false)
    private String publicId;
    private String name; // Nombre original del archivo (opcional, pero útil)
    private String url;
}