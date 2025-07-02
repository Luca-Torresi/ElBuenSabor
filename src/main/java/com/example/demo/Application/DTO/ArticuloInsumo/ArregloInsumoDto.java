package com.example.demo.Application.DTO.ArticuloInsumo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

//Envía una lista con los todos los insumos
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ArregloInsumoDto {
    private List<InsumoDto> insumos;
}
