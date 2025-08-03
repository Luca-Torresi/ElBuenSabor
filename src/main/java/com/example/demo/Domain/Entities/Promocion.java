package com.example.demo.Domain.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Promocion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPromocion;
    private String titulo;
    private String descripcion;
    private Double precio;
    private Boolean activo;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idImagen")
    private Imagen imagen;

    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL)
    private List<DetallePromocion> detalles;
}
