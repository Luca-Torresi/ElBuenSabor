package com.example.demo.Application.DTO.ArticuloManufacturado;

import com.example.demo.Application.DTO.Generic.ImagenDto;
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
    private int tiempoDeCocina;
    private boolean dadoDeBaja;
    private Long idCategoria;
    private ImagenDto imagenDto;
    private List<ArticuloManufacturadoDetalleDto> detalles;
}
