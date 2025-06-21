package com.example.demo.Domain.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data @SuperBuilder @AllArgsConstructor @NoArgsConstructor
@Entity
public class Empleado extends Usuario {

    // Si 'fechaBaja' es la fecha en que el empleado *dejó de trabajar* (historial laboral), está bien aquí.
    // Si es para la baja lógica de la cuenta de usuario, ya lo tenemos en 'activo' en Usuario.
    @Column(name = "fecha_baja_laboral") // <-- Opcional: nombre más descriptivo si es diferente a la baja de cuenta
    private LocalDate fechaBaja;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direccion") // <-- AGREGADO: Nombre de columna
    private Direccion direccion;
}