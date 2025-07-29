package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.ArticuloInsumo.*;
import com.example.demo.Application.Mapper.InsumoMapper;
import com.example.demo.Domain.Entities.*;
import com.example.demo.Domain.Exceptions.ArticuloNoEncontradoException;
import com.example.demo.Domain.Exceptions.InsumoNoEncontradoException;
import com.example.demo.Domain.Repositories.*;
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
    private final RepoArticulo repoArticulo;

    //Persiste un nuevo artículo insumo
    public ArticuloInsumo cargarNuevoInsumo(NuevoInsumoDto nuevoInsumoDto){
        Optional<UnidadDeMedida> unidaDeMedidaOpt = repoUnidadDeMedida.findById(nuevoInsumoDto.getIdUnidadDeMedida());
        Optional<RubroInsumo> rubroInsumoOpt = repoRubroInsumo.findById(nuevoInsumoDto.getIdRubroInsumo());

        ArticuloInsumo articuloInsumo = insumoMapper.articuloInsumoDtoToArticuloInsumo(nuevoInsumoDto);
        articuloInsumo.setUnidadDeMedida(unidaDeMedidaOpt.get());
        articuloInsumo.setRubroInsumo(rubroInsumoOpt.get());
        articuloInsumo.setFechaBaja(nuevoInsumoDto.isDadoDeAlta() ? null : LocalDate.now());

        ArticuloInsumo nuevoInsumo = repoArticuloInsumo.save(articuloInsumo);

        ActualizacionCosto actualizacionCosto = new ActualizacionCosto();
        actualizacionCosto.setArticuloInsumo(nuevoInsumo);
        actualizacionCosto.setCosto(nuevoInsumoDto.getCosto());
        actualizacionCosto.setFechaActualizacion(LocalDateTime.now());

        repoActualizacionCosto.save(actualizacionCosto);

        return nuevoInsumo;
    }

    //Para cada insumo del arreglo se suma al stock actual la cantidad ingresada
    public void recargaDeInsumos(ArregloRecargaInsumoDto arregloRecargaInsumoDto){
        for(RecargaInsumoDto insumo : arregloRecargaInsumoDto.getLista()){
            ArticuloInsumo articuloInsumo = repoArticuloInsumo.findById(insumo.getIdArticuloInsumo())
                    .orElseThrow(() -> new InsumoNoEncontradoException("No se encontró el insumo con ID: " + insumo.getIdArticuloInsumo()));

            articuloInsumo.setStockActual(articuloInsumo.getStockActual() + insumo.getCantidad());
            repoArticuloInsumo.save(articuloInsumo);
        }
    }

    //Devuelve una lista con todos los nombres de los insumos
    public ArregloInsumoDto listaInsumos(){
        List<ArticuloInsumo> articulosInsumo = repoArticuloInsumo.findByFechaBajaIsNull();

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
            InformacionInsumoDto dto = insumoMapper.articuloInsumoToInformacionInsumoDto(articulo);
            dto.setDadoDeAlta(articulo.getFechaBaja() != null ? false : true);

            ActualizacionCosto actualizacionCosto = repoActualizacionCosto.findTopByArticuloInsumoOrderByFechaActualizacionDesc(articulo);
            dto.setCosto(actualizacionCosto.getCosto());
            return dto;
        });
    }

    //Modifica la información de un artículo insumo
    public ArticuloInsumo modificarArticuloInsumo(Long idArticuloInsumo, InsumoModificacionDto dto){
        RubroInsumo rubroInsumo = repoRubroInsumo.findById(dto.getIdRubroInsumo()).get();
        UnidadDeMedida unidadDeMedida = repoUnidadDeMedida.findById(dto.getIdUnidadDeMedida()).get();

        ArticuloInsumo articuloInsumo = repoArticuloInsumo.findById(idArticuloInsumo)
                .orElseThrow(() -> new ArticuloNoEncontradoException("No se encontró el artículo con ID: " + idArticuloInsumo));

        articuloInsumo.setRubroInsumo(rubroInsumo);
        articuloInsumo.setUnidadDeMedida(unidadDeMedida);
        articuloInsumo.setFechaBaja(dto.isDadoDeAlta() ? null : LocalDate.now());

        ActualizacionCosto actualizacionCosto = ActualizacionCosto.builder()
                .articuloInsumo(articuloInsumo)
                .costo(dto.getCosto())
                .fechaActualizacion(LocalDateTime.now())
                .build();
        repoActualizacionCosto.save(actualizacionCosto);

        return repoArticuloInsumo.save(articuloInsumo);
    }

    //Dar de alta o baja a un insumo
    public void darDeAltaBaja(Long idArticuloInsumo) {
        ArticuloInsumo articuloInsumo = repoArticuloInsumo.findById(idArticuloInsumo)
                .orElseThrow(() -> new ArticuloNoEncontradoException("No se encontró el artículo con ID: " + idArticuloInsumo));
        articuloInsumo.setFechaBaja(
                articuloInsumo.getFechaBaja() != null ? null : LocalDate.now()
        );
        repoArticuloInsumo.save(articuloInsumo);
    }

    //Obtiene de la base de datos los artículos que contienen un insumo específico
    public ArregloIngredienteDto obtenerUsosInsumo(Long idArticuloInsumo){
        List<IngredienteDto> registros = repoArticuloInsumo.contienenDeterminadoInsumo(idArticuloInsumo);

        ArregloIngredienteDto arregloIngredienteDto = new ArregloIngredienteDto();

        List<IngredienteDto> ingredientes = new ArrayList<>();
        for(IngredienteDto ingredienteDto : registros){
            ingredientes.add(ingredienteDto);
        }
        arregloIngredienteDto.setIngredientes(ingredientes);
        return arregloIngredienteDto;
    }
}
