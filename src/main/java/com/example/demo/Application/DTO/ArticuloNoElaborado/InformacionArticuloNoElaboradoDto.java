package com.example.demo.Application.DTO.ArticuloNoElaborado;

import lombok.*;

//Envía y recibe los datos necesarios para el ABM
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class InformacionArticuloNoElaboradoDto {
    private Long idArticulo;
    private String nombre;
    private String descripcion;
    private double precioVenta;
    private boolean precioModificado;
    private boolean dadoDeAlta;
    private Long idCategoria;
    private String nombreCategoria;
    private String imagenUrl;
    private int stock;
    @Getter
    private boolean eliminarImagen;
}



