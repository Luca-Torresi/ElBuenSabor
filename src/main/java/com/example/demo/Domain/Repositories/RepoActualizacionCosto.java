package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.ActualizacionCosto;
import com.example.demo.Domain.Entities.ArticuloInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoActualizacionCosto extends JpaRepository<ActualizacionCosto, Long> {
    //Obtiene el último registro de la tabla 'ActualizacionCosto' correspondiente al insumo recibido como parámetro
    ActualizacionCosto findTopByArticuloInsumoOrderByFechaActualizacionDesc(ArticuloInsumo articuloInsumo);
}
