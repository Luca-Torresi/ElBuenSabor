package com.example.demo.Domain.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data @SuperBuilder @AllArgsConstructor @NoArgsConstructor
@Entity @Table @Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;

    @Column(unique = true)
    private String idAuth0;

    @ManyToOne
    @JoinColumn(name = "idRol")
    private Rol rol;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "idImagen")
    private Imagen imagen;
}