package com.example.demo.Application.DTO.ArticuloManufacturado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

//Envía y recibe la información necesaria de un artículo manufacturado para ser modificado
//Como también para mostrar sus detalles en el ABM
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class InformacionArticuloManufacturadoDto {
    private Long idArticulo;
    private String nombre;
    private String descripcion;
    private double precioVenta;
    private boolean precioModificado;
    private String receta;
    private int tiempoDeCocina;
    private boolean dadoDeAlta;
    private Long idCategoria;
    private String nombreCategoria;
    private String imagenUrl;
    private List<InformacionDetalleDto> detalles;
}
