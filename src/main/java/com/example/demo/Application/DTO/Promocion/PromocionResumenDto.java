package com.example.demo.Application.DTO.Promocion;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PromocionResumenDto {
    private Double precioBase;
    private Double precioPromocional;
    private Double ahorro;
}
