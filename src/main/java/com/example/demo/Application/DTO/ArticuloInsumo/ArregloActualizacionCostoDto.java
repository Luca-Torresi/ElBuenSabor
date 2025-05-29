package com.example.demo.Application.DTO.ArticuloInsumo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

//Recibe un arreglo con todos los insumos para los cuales se modificó su costo
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ArregloActualizacionCostoDto {
    private List<ActualizacionCostoDto> detalles;
}
