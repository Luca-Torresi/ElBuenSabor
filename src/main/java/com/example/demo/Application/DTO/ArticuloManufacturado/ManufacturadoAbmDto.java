package com.example.demo.Application.DTO.ArticuloManufacturado;

import com.example.demo.Application.DTO.Generic.ImagenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Envía los datos necesarios de los artículos manufacturados que se van a mostrar en el ABM
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ManufacturadoAbmDto {
    private Long idArticuloManufacturado;
    private String nombre;
    private double precioVenta;
    private boolean dadoDeAlta;
    private ImagenDto imagenDto;
}
