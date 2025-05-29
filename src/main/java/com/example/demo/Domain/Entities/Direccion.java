package com.example.demo.Domain.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data @SuperBuilder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Direccion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDireccion;

    @ManyToOne @JoinColumn(name = "idDepartamento")
    private Departamento departamento;

    private String calle;
    private String numero;
    private String piso;
    private String dpto;
}
