package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.UnidadDeMedida.ArregloUnidadDeMedidaDto;
import com.example.demo.Domain.Repositories.RepoUnidadDeMedida;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceUnidadDeMedida {
    private final RepoUnidadDeMedida repoUnidadDeMedida;

    //Obtiene de la base de datos todas las unidades de medida
    public ArregloUnidadDeMedidaDto listarUnidadesDeMedida(){
        ArregloUnidadDeMedidaDto arreglo = new ArregloUnidadDeMedidaDto();
        arreglo.setLista(repoUnidadDeMedida.findAll());
        return arreglo;
    }
}
