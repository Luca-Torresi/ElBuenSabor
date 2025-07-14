package com.example.demo.Application.DTO.RubroInsumo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RubroInsumoCompletoDto {
    private Long idRubroInsumo;
    private String nombre;
    private boolean dadoDeAlta;
    private Long idRubroPadre;
    private String rubroPadre;
}
