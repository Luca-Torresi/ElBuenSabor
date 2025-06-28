package com.example.demo.Application.DTO.Categoria;

import com.example.demo.Application.DTO.Generic.ImagenModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CategoriaDto {
    private Long idCategoria;
    private String nombre;
    private Long idCategoriaPadre;
    private ImagenModel imagenModel;
    private LocalDate fechaBaja;
    private double margenGanancia;

}
