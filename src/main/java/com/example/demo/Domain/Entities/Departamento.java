package com.example.demo.Domain.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data @SuperBuilder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Departamento {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDepartamento;
    private String nombre;

    @ManyToOne @JoinColumn(name = "idProvincia")
    private Provincia provincia;
}
