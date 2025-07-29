package com.example.demo.Application.DTO.RubroInsumo;

import com.example.demo.Application.DTO.ArticuloInsumo.IngredienteDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RubroInsumoCompletoDto {
    private Long idRubroInsumo;
    private String nombre;
    private boolean dadoDeAlta;
    private Long idRubroPadre;
    private String rubroPadre;
    private int cantInsumos;
    private List<String> insumos;
}
