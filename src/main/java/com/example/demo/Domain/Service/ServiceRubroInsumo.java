package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.RubroInsumo.ArregloRubroInsumoDto;
import com.example.demo.Application.DTO.RubroInsumo.NuevoRubroInsumoDto;
import com.example.demo.Application.DTO.RubroInsumo.RubroInsumoDto;
import com.example.demo.Domain.Entities.RubroInsumo;
import com.example.demo.Domain.Repositories.RepoRubroInsumo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceRubroInsumo {
    private final RepoRubroInsumo repoRubroInsumo;

    //Persiste en la base de datos un nuevo Rubro Insumo
    public void nuevoRubro(NuevoRubroInsumoDto nuevoRubroInsumoDto) {
        RubroInsumo rubroInsumoPadre = repoRubroInsumo.findById(nuevoRubroInsumoDto.getIdRubroInsumoPadre()).get();

        RubroInsumo rubroInsumo = RubroInsumo.builder()
                .nombre(nuevoRubroInsumoDto.getNombre())
                .fechaBaja(nuevoRubroInsumoDto.isDadoDeBaja() ? LocalDate.now() : null)
                .rubroInsumoPadre(rubroInsumoPadre)
                .build();
        repoRubroInsumo.save(rubroInsumo);
    }

    //Devuelve un arreglo con los nombres de los rubros insumo
    public ArregloRubroInsumoDto listarRubrosInsumo(){
        ArregloRubroInsumoDto arreglo = new ArregloRubroInsumoDto();

        List<RubroInsumoDto> rubrosDto = new ArrayList<>();

        List<RubroInsumo> rubros = repoRubroInsumo.findAll();
        for(RubroInsumo rubro: rubros){
            RubroInsumoDto rubroDto = new RubroInsumoDto();
            rubroDto.setIdRubroInsumo(rubro.getIdRubroInsumo());
            rubroDto.setNombre(rubro.getNombre());

            rubrosDto.add(rubroDto);
        }
        arreglo.setArregloRubros(rubrosDto);
        return arreglo;
    }
}
