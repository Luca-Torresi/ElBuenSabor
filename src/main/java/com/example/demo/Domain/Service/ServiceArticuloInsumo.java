package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.ArticuloInsumo.*;
import com.example.demo.Application.Mapper.InsumoMapper;
import com.example.demo.Domain.Entities.*;
import com.example.demo.Domain.Repositories.RepoActualizacionCosto;
import com.example.demo.Domain.Repositories.RepoArticuloInsumo;
import com.example.demo.Domain.Repositories.RepoRubroInsumo;
import com.example.demo.Domain.Repositories.RepoUnidadDeMedida;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceArticuloInsumo {
    private final RepoActualizacionCosto repoActualizacionCosto;
    private final RepoArticuloInsumo repoArticuloInsumo;
    private final RepoUnidadDeMedida repoUnidadDeMedida;
    private final RepoRubroInsumo repoRubroInsumo;
    private final InsumoMapper insumoMapper;

    //Realiza un 'insert' de los registros con el nuevo costo por cada insumo recibido
    public void actualizarCostos(ArregloActualizacionCostoDto arregloActualizacionCostoDto){
        for(ActualizacionCostoDto detalle: arregloActualizacionCostoDto.getDetalles()){
            Optional<ArticuloInsumo> articuloInsumoOpt = repoArticuloInsumo.findById(detalle.getArticuloInsumo());

            ActualizacionCosto actualizacionCosto = ActualizacionCosto.builder()
                    .articuloInsumo(articuloInsumoOpt.get())
                    .costo(detalle.getCosto())
                    .fechaActualizacion(LocalDateTime.now())
                    .build();
            repoActualizacionCosto.save(actualizacionCosto);
        }
    }

    //Persiste un nuevo artículo insumo
    public void cargarNuevoInsumo(NuevoArticuloInsumoDto nuevoArticuloInsumoDto){
        Optional<UnidadDeMedida> unidaDeMedidaOpt = repoUnidadDeMedida.findById(nuevoArticuloInsumoDto.getIdUnidadDeMedida());
        Optional<RubroInsumo> rubroInsumoOpt = repoRubroInsumo.findById(nuevoArticuloInsumoDto.getIdRubroInsumo());

        ArticuloInsumo articuloInsumo = insumoMapper.articuloInsumoDtoToArticuloInsumo(nuevoArticuloInsumoDto);
        articuloInsumo.setUnidadDeMedida(unidaDeMedidaOpt.get());
        articuloInsumo.setRubroInsumo(rubroInsumoOpt.get());
        articuloInsumo.setFechaBaja(nuevoArticuloInsumoDto.isDadoDeBaja() ? LocalDate.now() : null);

        repoArticuloInsumo.save(articuloInsumo);
    }

    //Para cada insumo del arreglo se suma al stock actual la cantidad ingresada
    public void recargaDeInsumos(ArregloRecargaInsumoDto arregloRecargaInsumoDto){
        for(RecargaInsumoDto insumo : arregloRecargaInsumoDto.getLista()){
            ArticuloInsumo articuloInsumo = repoArticuloInsumo.findById(insumo.getIdArticuloInsumo()).get();

            articuloInsumo.setStockActual(articuloInsumo.getStockActual() + insumo.getCantidad());
            repoArticuloInsumo.save(articuloInsumo);
        }
    }
}
