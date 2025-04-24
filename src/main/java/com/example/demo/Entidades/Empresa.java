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
public class Empresa {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmpresa;
    private String nombre;
    private String razonSocial;
    private String cuil;

    @OneToMany(mappedBy = "empresa")
    @JsonManagedReference
    private List<Sucursal> sucursales;
}
