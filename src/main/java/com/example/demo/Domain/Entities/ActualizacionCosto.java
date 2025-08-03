package com.example.demo.Domain.Entities;

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
    private Double costo;
    private LocalDateTime fechaActualizacion;

    @ManyToOne
    @JoinColumn(name = "idArticuloInsumo")
    private ArticuloInsumo articuloInsumo;
}
