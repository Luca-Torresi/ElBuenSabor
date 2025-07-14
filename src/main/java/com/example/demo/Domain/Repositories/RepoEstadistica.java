package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Pedido;
import com.example.demo.Domain.Projections.ColumnChartProjection;
import com.example.demo.Domain.Projections.LineChartProjection;
import com.example.demo.Domain.Projections.MultipleLineChartProjection;
import com.example.demo.Domain.Projections.PieChartProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepoEstadistica extends JpaRepository<Pedido, Long> {
    @Query(value = "SELECT nombreArticulo, cantVendido FROM articulosMasVendidos where nombreCategoria = :categoria", nativeQuery = true)
    List<PieChartProjection> obtenerArticulosMasVendidos(@Param("categoria") String categoria);

    @Query(value = "SELECT hora, porcentaje FROM promedioPedidosPorHora", nativeQuery = true)
    List<ColumnChartProjection> obtenerPromedioPedidosPorHora();

    @Query(value = "SELECT mes, totalRecaudado FROM recaudadoPorMes", nativeQuery = true)
    List<LineChartProjection> obtenerRecaudadoPorMes();

    @Query(value = "SELECT mes, clientesSinPedido, clientesConUnPedido, clientesRecurrentes FROM frecuenciaClientes", nativeQuery = true)
    List<MultipleLineChartProjection> obtenerFrecuenciaClientes();
}
