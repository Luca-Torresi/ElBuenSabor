package com.example.demo.Application.DTO.UnidadDeMedida;

import com.example.demo.Domain.Entities.UnidadDeMedida;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ArregloUnidadDeMedidaDto {
    private List<UnidadDeMedida> lista;
}
