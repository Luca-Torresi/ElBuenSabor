package com.example.demo.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class ActualizacionCosto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActualizacionCosto;
    private double nuevoCosto;
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "idInsumo")
    private Insumo insumo;
}
