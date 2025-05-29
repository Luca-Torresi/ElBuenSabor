package com.example.demo.Application.DTO.ArticuloManufacturado;

import com.example.demo.Application.DTO.Generic.ImagenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Contiene la información necesaria de los artículos que van a ser mostrados en el catálogo
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ArticuloManufacturadoDto {
    private Long idArticuloManufacturado;
    private String nombre;
    private String descripcion;
    private double precioVenta;
    private int tiempoDeCocina;
    private Long idCategoria;
    private ImagenDto imagenDto;
    private boolean puedeElaborarse;
}
