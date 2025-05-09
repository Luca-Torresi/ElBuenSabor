package com.example.demo.Application.DTO.Insumo;

import com.example.demo.Domain.Entities.ArticuloInsumo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ActualizacionCostoDto {
    private Long articuloInsumo;
    private double costo;
    private LocalDateTime fechaActualizacion;
}
