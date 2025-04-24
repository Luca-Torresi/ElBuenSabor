package com.example.demo.Entidades;

import com.example.demo.Enumeraciones.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Empleado {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpleado;
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private String telefono;

    @Enumerated(EnumType.STRING)
    private Rol rol;
}
