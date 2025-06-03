package com.example.demo.Application.DTO.ArticuloNoElaborado;

import com.example.demo.Application.DTO.Generic.ImagenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NuevoArticuloNoElaboradoDto {
    private String nombre;
    private String descripcion;
    private double precioVenta;
    private boolean dadoDeAlta;
    private Long idCategoria;
    private ImagenDto imagenDto;
}
