package com.example.demo.Application.DTO.ArticuloNoElaborado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RecargaNoElaboradoDto {
    private Long idArticulo;
    private int cantidad;
}
