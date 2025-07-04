package com.example.demo.Application.DTO.ArticuloManufacturado;

import com.example.demo.Application.DTO.Generic.ImagenModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

//Recibe la información necesaria para la carga de un nuevo artículo
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NuevoArticuloManufacturadoDto {
    private String nombre;
    private String descripcion;
    private String receta;
    private double precioVenta;
    private int tiempoDeCocina;
    private boolean dadoDeAlta;
    private Long idCategoria;
    private List<ArticuloManufacturadoDetalleDto> detalles;
}
