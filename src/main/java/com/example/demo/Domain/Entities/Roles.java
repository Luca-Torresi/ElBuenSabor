package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder; // Asegúrate de tener esta importación
import lombok.Data;
import lombok.NoArgsConstructor;
// import lombok.experimental.SuperBuilder; // <-- Generalmente no es necesario si Roles no es padre de otras entidades

import java.util.HashSet;
import java.util.Set;

@Data
@Builder // <--- Mantén solo @Builder si Roles no tiene herencia de otra clase con Builder
@NoArgsConstructor
@AllArgsConstructor // <--- Necesario para el constructor completo usado en el DataLoader
@Entity
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID interno de tu BD para el rol

    @Column(unique = true, nullable = false)
    private String name; // Nombre del rol, ej: "admin", "cliente", "cocinero", "cajero"

    @Column(unique = true, nullable = false)
    private String auth0RoleId; // ID del rol en Auth0 (es una cadena larga)

    // Relación Many-to-Many con Usuario (asegúrate de que Usuario también tenga esta relación)
    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    @Builder.Default // Asegura que HashSet se inicialice con el builder
    private Set<Usuario> usuarios = new HashSet<>();
}