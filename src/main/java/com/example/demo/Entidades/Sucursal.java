package com.example.demo.Entidades;

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

    @ManyToOne
    @JoinColumn(name = "idEmpresa")
    @JsonBackReference
    private Empresa empresa;

    @ManyToMany
    @JoinTable(
            name = "sucursal_categoria",
            joinColumns = @JoinColumn(name = "id_sucursal"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private List<Categoria> categorias;
}
