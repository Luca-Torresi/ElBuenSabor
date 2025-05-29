package com.example.demo.Application.DTO.ArticuloInsumo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

//Contiene el nuevo costo para cada insumo del 'ArregloActualizacionCostoDto'
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ActualizacionCostoDto {
    private Long articuloInsumo;
    private double costo;
}
