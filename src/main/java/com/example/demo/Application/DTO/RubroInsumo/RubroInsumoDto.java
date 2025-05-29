package com.example.demo.Application.DTO.RubroInsumo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Para la creación de un nuevo rubro insumo
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RubroInsumoDto {
    private String nombre;
    private boolean dadoDeBaja;
    private Long idRubroInsumoPadre;
}
