package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private String calle;
    private String numero;
    private String piso;
    private String dpto;

    @ManyToOne @JoinColumn(name = "idDepartamento")
    private Departamento departamento;

    @ManyToOne @JoinColumn(name = "idCliente")
    @JsonBackReference
    private Cliente cliente;
}
