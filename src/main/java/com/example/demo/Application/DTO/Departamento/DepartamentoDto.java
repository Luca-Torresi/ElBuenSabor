package com.example.demo.Application.DTO.Departamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartamentoDto {
    private Long id;
    private String nombre;

}
