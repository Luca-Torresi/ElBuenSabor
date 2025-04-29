package com.example.demo.Domain.Entities;

import com.example.demo.Domain.Enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data @SuperBuilder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Empleado extends Usuario {

    @Enumerated(EnumType.STRING)
    private Rol rol;
}
