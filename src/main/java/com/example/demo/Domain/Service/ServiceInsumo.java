package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Insumo.ActualizacionCostoDto;
import com.example.demo.Application.DTO.Insumo.ArregloActualizacionCostoDto;
import com.example.demo.Domain.Entities.ActualizacionCosto;
import com.example.demo.Domain.Entities.ArticuloInsumo;
import com.example.demo.Domain.Repositories.RepoActualizacionCosto;
import com.example.demo.Domain.Repositories.RepoArticuloInsumo;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ServiceInsumo {

    private final RepoActualizacionCosto repoActualizacionCosto;
    private final RepoArticuloInsumo repoArticuloInsumo;

    public ServiceInsumo(RepoActualizacionCosto repoActualizacionCosto, RepoArticuloInsumo repoArticuloInsumo) {
        this.repoActualizacionCosto = repoActualizacionCosto;
        this.repoArticuloInsumo = repoArticuloInsumo;
    }

    public void actualizarCostos(ArregloActualizacionCostoDto arregloActualizacionCostoDto){
        for(ActualizacionCostoDto detalle: arregloActualizacionCostoDto.getDetalles()){
            ArticuloInsumo articuloInsumo = repoArticuloInsumo.findByIdArticuloInsumo(detalle.getArticuloInsumo());

            ActualizacionCosto actualizacionCosto = ActualizacionCosto.builder()
                    .articuloInsumo(articuloInsumo)
                    .costo(detalle.getCosto())
                    .fechaActualizacion(LocalDateTime.now())
                    .build();
            repoActualizacionCosto.save(actualizacionCosto);
        }
    }
}
