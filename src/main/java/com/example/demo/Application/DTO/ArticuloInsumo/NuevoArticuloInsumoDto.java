package com.example.demo.Application.DTO.ArticuloInsumo;

import com.example.demo.Application.DTO.Generic.ImagenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Recibe la información necesaria para la creación de un nuevo artículo insumo
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NuevoArticuloInsumoDto {
    private String nombre;
    private double stockActual;
    private double stockMinimo;
    private double stockMaximo;
    private boolean dadoDeBaja;
    private Long idUnidadDeMedida;
    private Long idRubroInsumo;
    private ImagenDto imagenDto;
}
