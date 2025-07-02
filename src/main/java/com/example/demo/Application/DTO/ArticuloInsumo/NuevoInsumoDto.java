package com.example.demo.Application.DTO.ArticuloInsumo;

import com.example.demo.Application.DTO.Generic.ImagenModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Recibe la información necesaria para la creación de un nuevo artículo insumo
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NuevoInsumoDto {
    private String nombre;
    private double stockMinimo;
    private double stockMaximo;
    private boolean dadoDeAlta;
    private Long idUnidadDeMedida;
    private Long idRubroInsumo;
}
