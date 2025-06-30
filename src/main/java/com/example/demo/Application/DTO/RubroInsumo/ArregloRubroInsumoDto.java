package com.example.demo.Application.DTO.RubroInsumo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ArregloRubroInsumoDto {
    private List<RubroInsumoDto> arregloRubros;
}
