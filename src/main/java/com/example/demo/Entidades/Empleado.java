package com.example.demo.Entidades;

import com.example.demo.Enumeraciones.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Empleado extends Usuario {

    @Enumerated(EnumType.STRING)
    private Rol rol;
}
