package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.RubroInsumo.*;
import com.example.demo.Application.Mapper.RubroInsumoMapper;
import com.example.demo.Domain.Entities.ArticuloInsumo;
import com.example.demo.Domain.Entities.Categoria;
import com.example.demo.Domain.Entities.RubroInsumo;
import com.example.demo.Domain.Repositories.RepoArticuloInsumo;
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
    private final RepoArticuloInsumo repoArticuloInsumo;

    //Persiste en la base de datos un nuevo Rubro Insumo
    public RubroInsumo nuevoRubro(NuevoRubroInsumoDto dto) {

        RubroInsumo rubroInsumo = RubroInsumo.builder()
                .nombre(dto.getNombre())
                .fechaBaja(dto.isDadoDeAlta() ? null : LocalDate.now())
                .build();

        if(dto.getIdRubroInsumoPadre() != null){
            RubroInsumo padre = repoRubroInsumo.findById(dto.getIdRubroInsumoPadre()).get();
            rubroInsumo.setRubroInsumoPadre(padre);
        } else {
            rubroInsumo.setRubroInsumoPadre(null);
        }

        return repoRubroInsumo.save(rubroInsumo);
    }

    //Devuelve un arreglo con los nombres de los rubros insumo
    public List<RubroInsumoDto> listarRubrosInsumo(){
        List<RubroInsumoDto> rubrosDto = new ArrayList<>();

        List<RubroInsumo> rubros = repoRubroInsumo.findAll();
        for(RubroInsumo rubro: rubros){
            RubroInsumoDto rubroDto = new RubroInsumoDto();
            rubroDto.setIdRubroInsumo(rubro.getIdRubroInsumo());
            rubroDto.setNombre(rubro.getNombre());

            rubrosDto.add(rubroDto);
        }
        return rubrosDto;
    }

    //Devuelve todos los rubro insumo para ser mostrados en el ABM
    public List<RubroInsumoCompletoDto> abmRubrosInsumo(){
        List<RubroInsumo> rubros = repoRubroInsumo.findAll();

        List<RubroInsumoCompletoDto> lista = new ArrayList<>();

        for(RubroInsumo rubro: rubros){
            RubroInsumoCompletoDto dto = rubroInsumoMapper.rubroInsumoToRubroInsumoCompletoDto(rubro);
            dto.setDadoDeAlta(rubro.getFechaBaja() != null ? false : true);

            List<String> insumos = repoArticuloInsumo.findNombresByIdRubroInsumo(rubro.getIdRubroInsumo());
            dto.setInsumos(insumos);
            dto.setCantInsumos(insumos.size());

            lista.add(dto);
        }
        return lista;
    }

    //Dar de alta o baja a un rubro insumo
    public void darDeAltaBaja(Long idRubroInsumo) {
        RubroInsumo rubroInsumo = repoRubroInsumo.findById(idRubroInsumo).get();
        rubroInsumo.setFechaBaja(
                rubroInsumo.getFechaBaja() != null ? null : LocalDate.now()
        );
        repoRubroInsumo.save(rubroInsumo);
    }

    //Actualiza los datos del rubro
    public RubroInsumo modificarRubroInsumo(Long idRubroInsumo, NuevoRubroInsumoDto dto) {
        RubroInsumo rubroInsumo = repoRubroInsumo.findById(idRubroInsumo).get();

        rubroInsumo.setNombre(dto.getNombre());
        rubroInsumo.setFechaBaja(dto.isDadoDeAlta() ? null : LocalDate.now());

        if(dto.getIdRubroInsumoPadre() != null){
            RubroInsumo padre = repoRubroInsumo.findById(dto.getIdRubroInsumoPadre()).get();
            rubroInsumo.setRubroInsumoPadre(padre);
        } else {
            rubroInsumo.setRubroInsumoPadre(null);
        }

        return repoRubroInsumo.save(rubroInsumo);
    }
}
