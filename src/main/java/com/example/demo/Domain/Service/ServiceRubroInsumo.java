package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.RubroInsumo.RubroInsumoDto;
import com.example.demo.Domain.Entities.RubroInsumo;
import com.example.demo.Domain.Repositories.RepoRubroInsumo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ServiceRubroInsumo {
    private final RepoRubroInsumo repoRubroInsumo;

    //Persiste en la base de datos un nuevo Rubro Insumo
    public void nuevoRubro(RubroInsumoDto rubroInsumoDto) {
        RubroInsumo rubroInsumoPadre = repoRubroInsumo.findById(rubroInsumoDto.getIdRubroInsumoPadre()).get();

        RubroInsumo rubroInsumo = RubroInsumo.builder()
                .nombre(rubroInsumoDto.getNombre())
                .fechaBaja(rubroInsumoDto.isDadoDeBaja() ? LocalDate.now() : null)
                .rubroInsumoPadre(rubroInsumoPadre)
                .build();
        repoRubroInsumo.save(rubroInsumo);
    }
}
