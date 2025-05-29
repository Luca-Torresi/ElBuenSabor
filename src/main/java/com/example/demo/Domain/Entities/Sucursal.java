package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Sucursal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSucursal;
    private String nombre;
    private LocalTime horarioApertura;
    private LocalTime horarioCierre;

    @OneToOne @JoinColumn(name = "idDireccion")
    private Direccion direccion;

    @ManyToOne @JoinColumn(name = "idEmpresa")
    @JsonBackReference
    private Empresa empresa;
}
