package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Pedido;
import com.example.demo.Domain.Projections.ColumnChartProjection;
import com.example.demo.Domain.Projections.PieChartProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepoEstadistica extends JpaRepository<Pedido, Long> {
    @Query(value = "SELECT nombre, cantVendido FROM articulosMasVendidos", nativeQuery = true)
    List<PieChartProjection> obtenerArticulosMasVendidos();

    @Query(value = "SELECT hora, porcentaje FROM promedioVentasPorHora", nativeQuery = true)
    List<ColumnChartProjection> obtenerPromedioVentasPorHora();
}
