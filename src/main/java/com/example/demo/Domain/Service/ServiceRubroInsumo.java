package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.RubroInsumo.*;
import com.example.demo.Application.Mapper.RubroInsumoMapper;
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
    private final RubroInsumoMapper rubroInsumoMapper;

    //Persiste en la base de datos un nuevo Rubro Insumo
    public RubroInsumo nuevoRubro(NuevoRubroInsumoDto nuevoRubroInsumoDto) {
        RubroInsumo rubroInsumoPadre = repoRubroInsumo.findById(nuevoRubroInsumoDto.getIdRubroInsumoPadre()).get();

        RubroInsumo rubroInsumo = RubroInsumo.builder()
                .nombre(nuevoRubroInsumoDto.getNombre())
                .fechaBaja(nuevoRubroInsumoDto.isDadoDeAlta() ? null : LocalDate.now())
                .rubroInsumoPadre(rubroInsumoPadre)
                .build();
        return repoRubroInsumo.save(rubroInsumo);
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

    //Devuelve todos los rubro insumo para ser mostrados en el ABM
    public ArregloRubroInsumoCompletoDto abmRubrosInsumo(){
        List<RubroInsumo> rubros = repoRubroInsumo.findAll();

        List<RubroInsumoCompletoDto> rubrosCompletos = new ArrayList<>();

        for(RubroInsumo rubro: rubros){
            RubroInsumoCompletoDto dto = rubroInsumoMapper.rubroInsumoToRubroInsumoCompletoDto(rubro);
            rubrosCompletos.add(dto);
        }
        ArregloRubroInsumoCompletoDto arreglo = new ArregloRubroInsumoCompletoDto();
        arreglo.setRubrosDto(rubrosCompletos);
        return arreglo;
    }
}
