package com.example.demo.Application.DTO.Categoria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Para modificar el margen de ganancia de una categoría
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NuevoMargenGananciaDto {
    private Long idCategoria;
    private double margenGanancia;
}
