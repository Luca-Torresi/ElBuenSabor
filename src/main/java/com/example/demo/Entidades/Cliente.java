package com.example.demo.Entidades;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

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

    @OneToMany(mappedBy = "cliente")
    @JsonManagedReference
    private List<Pedido> pedidos;
}
