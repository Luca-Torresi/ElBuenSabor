package com.example.demo.Application.DTO.Categoria;

import com.example.demo.Application.DTO.Generic.ImagenModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Recibe la información necesaria para la creación de una nueva categoría
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NuevaCategoriaDto {
    private String nombre;
    private double margenGanancia;
    private boolean dadoDeAlta;
    private Long idCategoriaPadre;
}
