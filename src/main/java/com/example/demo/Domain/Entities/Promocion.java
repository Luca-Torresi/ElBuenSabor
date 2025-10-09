package com.example.demo.Domain.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Promocion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPromocion")
    private Long idPromocion;

    private String titulo;
    private String descripcion;

    // Precio definido por el admin (ej: combo pizza + gaseosa $3000)
    private Double precioPromocion;

    // Horarios en la base de datos tipo TIME
    @Getter @Setter
    private LocalTime horarioInicio;
    @Getter @Setter
    private LocalTime horarioFin;

    private Boolean activo;

    private Integer tiempoDeCocina; // opcional

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idImagen")
    private Imagen imagen;

    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePromocion> detalles;
}
