package com.example.demo.Application.DTO.Articulo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ArticuloNombreDto {
    private Long idArticulo;
    private String nombre;
}
