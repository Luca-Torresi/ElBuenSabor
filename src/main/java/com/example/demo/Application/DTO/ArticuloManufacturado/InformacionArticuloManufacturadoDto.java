package com.example.demo.Application.DTO.ArticuloManufacturado;

import com.example.demo.Application.DTO.Generic.ImagenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

//Envía la información correspondiente a un artículo manufacturado para ser editado o para ver los detalles en el ABM
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class InformacionArticuloManufacturadoDto {
    private String nombre;
    private String descripcion;
    private String receta;
    private int tiempoDeCocina;
    private ImagenDto imagenDto;
    private Long idCategoria;
    private List<InformacionDetalleDto> detalles;
}
