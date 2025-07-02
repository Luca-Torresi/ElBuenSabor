package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Entity @Table
public class Rol {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String auth0RoleId;

    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JsonIgnore @Builder.Default
    private Set<Usuario> usuarios = new HashSet<>();
}