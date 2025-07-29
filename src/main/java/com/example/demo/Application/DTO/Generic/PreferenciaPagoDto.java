package com.example.demo.Application.DTO.Generic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreferenciaPagoDto {
    private List<ItemDTO> items;
    private Double costoEnvio;
}
