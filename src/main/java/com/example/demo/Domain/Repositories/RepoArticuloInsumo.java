package com.example.demo.Domain.Repositories;

import com.example.demo.Application.DTO.ArticuloInsumo.IngredienteDto;
import com.example.demo.Domain.Entities.ArticuloInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoArticuloInsumo extends JpaRepository<ArticuloInsumo, Long> {
    @Query(value = "SELECT articulo.nombre, cantidad FROM articuloManufacturadoDetalle amd INNER JOIN articulo ON articulo.idArticulo = amd.idArticulo WHERE idArticuloInsumo = :idArticuloInsumo", nativeQuery = true)
    List<IngredienteDto> contienenDeterminadoInsumo(@Param("idArticuloInsumo") Long idArticuloInsumo);

    @Query(value = "SELECT nombre FROM articuloInsumo WHERE idRubroInsumo = :idRubroInsumo", nativeQuery = true)
    List<String> findNombresByIdRubroInsumo(@Param("idRubroInsumo") Long idRubroInsumo);
}
