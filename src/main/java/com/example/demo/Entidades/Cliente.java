package com.example.demo.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Cliente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String password;
    private String imagen;
}
