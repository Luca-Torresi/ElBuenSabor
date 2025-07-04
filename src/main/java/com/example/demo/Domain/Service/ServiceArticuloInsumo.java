package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.ArticuloInsumo.*;
import com.example.demo.Application.DTO.ArticuloManufacturado.InformacionArticuloManufacturadoDto;
import com.example.demo.Application.Mapper.InsumoMapper;
import com.example.demo.Domain.Entities.*;
import com.example.demo.Domain.Repositories.RepoActualizacionCosto;
import com.example.demo.Domain.Repositories.RepoArticuloInsumo;
import com.example.demo.Domain.Repositories.RepoRubroInsumo;
import com.example.demo.Domain.Repositories.RepoUnidadDeMedida;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceArticuloInsumo {
    private final RepoActualizacionCosto repoActualizacionCosto;
    private final RepoArticuloInsumo repoArticuloInsumo;
    private final RepoUnidadDeMedida repoUnidadDeMedida;
    private final RepoRubroInsumo repoRubroInsumo;
    private final InsumoMapper insumoMapper;

    //Persiste un nuevo artículo insumo
    public ArticuloInsumo cargarNuevoInsumo(NuevoInsumoDto nuevoInsumoDto){
        Optional<UnidadDeMedida> unidaDeMedidaOpt = repoUnidadDeMedida.findById(nuevoInsumoDto.getIdUnidadDeMedida());
        Optional<RubroInsumo> rubroInsumoOpt = repoRubroInsumo.findById(nuevoInsumoDto.getIdRubroInsumo());

        ArticuloInsumo articuloInsumo = insumoMapper.articuloInsumoDtoToArticuloInsumo(nuevoInsumoDto);
        articuloInsumo.setUnidadDeMedida(unidaDeMedidaOpt.get());
        articuloInsumo.setRubroInsumo(rubroInsumoOpt.get());
        articuloInsumo.setFechaBaja(nuevoInsumoDto.isDadoDeAlta() ? null : LocalDate.now());

        return repoArticuloInsumo.save(articuloInsumo);
    }

    //Para cada insumo del arreglo se suma al stock actual la cantidad ingresada
    public void recargaDeInsumos(ArregloRecargaInsumoDto arregloRecargaInsumoDto){
        for(RecargaInsumoDto insumo : arregloRecargaInsumoDto.getLista()){
            ArticuloInsumo articuloInsumo = repoArticuloInsumo.findById(insumo.getIdArticuloInsumo()).get();

            articuloInsumo.setStockActual(articuloInsumo.getStockActual() + insumo.getCantidad());
            repoArticuloInsumo.save(articuloInsumo);
        }
    }

    //Devuelve una lista con todos los nombres de los insumos
    public ArregloInsumoDto listaInsumos(){
        List<ArticuloInsumo> articulosInsumo = repoArticuloInsumo.findAll();

        List<InsumoDto> insumos = new ArrayList<>();
        for(ArticuloInsumo insumo : articulosInsumo){
            ActualizacionCosto actualizacionCosto = repoActualizacionCosto.findTopByArticuloInsumoOrderByFechaActualizacionDesc(insumo);

            InsumoDto insumoDto = InsumoDto.builder()
                    .idArticuloInsumo(insumo.getIdArticuloInsumo())
                    .nombre(insumo.getNombre())
                    .unidadDeMedida(insumo.getUnidadDeMedida().getNombre())
                    .costo(actualizacionCosto.getCosto())
                    .build();
            insumos.add(insumoDto);
        }
        ArregloInsumoDto arregloInsumoDto = new ArregloInsumoDto();
        arregloInsumoDto.setInsumos(insumos);

        return arregloInsumoDto;
    }

    //Devuelve una lista para ser mostrada en el ABM de artículos insumo
    public Page<InformacionInsumoDto> listarArticulosABM(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticuloInsumo> paginaArticulosAbm = repoArticuloInsumo.findAll(pageable);

        return paginaArticulosAbm.map(articulo -> {
            InformacionInsumoDto dto = insumoMapper.articuloInsumoToInformacionInsumo(articulo);
            dto.setDadoDeAlta(articulo.getFechaBaja() != null ? false : true);
            return dto;
        });
    }
}
