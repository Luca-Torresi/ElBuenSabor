package com.example.demo.Application.DTO.ArticuloNoElaborado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NoElaboradoNombreDto {
    private Long idArticulo;
    private String nombre;
}
