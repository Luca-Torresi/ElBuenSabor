package com.example.demo.Domain.Entities;

import com.example.demo.Domain.Enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data @SuperBuilder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Empleado extends Usuario {

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private LocalDate fechaBaja;
}
