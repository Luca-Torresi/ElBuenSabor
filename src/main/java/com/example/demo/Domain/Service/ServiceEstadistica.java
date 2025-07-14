package com.example.demo.Domain.Service;

import com.example.demo.Domain.Projections.ColumnChartProjection;
import com.example.demo.Domain.Projections.LineChartProjection;
import com.example.demo.Domain.Projections.MultipleLineChartProjection;
import com.example.demo.Domain.Projections.PieChartProjection;
import com.example.demo.Domain.Repositories.RepoEstadistica;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceEstadistica {
    private final RepoEstadistica repoEstadistica;

    //Obtiene la información de la VIEW 'artículosMasVendidos' para mostrar estos datos en un gráfico de torta
    public List<List<Object>> pieChart(String categoria){
        List<List<Object>> resultado = new ArrayList<>();

        resultado.add(List.of("Artículo", "Cantidad de ventas"));

        List<PieChartProjection> datos = repoEstadistica.obtenerArticulosMasVendidos(categoria);
        for (PieChartProjection dato : datos) {
            resultado.add(List.of(dato.getNombreArticulo(), dato.getCantVendido()));
        }
        return resultado;
    }

    //Obtiene la información de la VIEW 'promedioVentasPorHora' para mostrar estos datos en un gráfico de columnas
    public List<List<Object>> ColumnChart(){
        List<List<Object>> resultado = new ArrayList<>();

        resultado.add(List.of("Hora", "Porcentaje de ventas diarias"));

        List<ColumnChartProjection> datos = repoEstadistica.obtenerPromedioPedidosPorHora();
        for (ColumnChartProjection dato : datos) {
            resultado.add(List.of(dato.getHora(), dato.getPorcentaje()));
        }
        return resultado;
    }

    //Obtiene la información de la VIEW 'recaudadoPorMes' para mostrar estos datos en un gráfico de líneas
    public List<List<Object>> LineChart(){
        List<List<Object>> resultado = new ArrayList<>();

        resultado.add(List.of("Mes", "Total recaudado"));

        List<LineChartProjection> datos = repoEstadistica.obtenerRecaudadoPorMes();
        for (LineChartProjection dato : datos) {
            resultado.add(List.of(dato.getMes(), dato.getTotalRecaudado()));
        }
        return resultado;
    }

    //Obtiene la información de la VIEW 'frecuenciaClientes'
    public List<List<Object>> multipleLineChart(){
        List<List<Object>> resultado = new ArrayList<>();

        resultado.add(List.of("Mes", "Sin pedidos", "Un solo pedido", "Más de un pedido"));
        List<MultipleLineChartProjection> datos = repoEstadistica.obtenerFrecuenciaClientes();
        for (MultipleLineChartProjection dato : datos) {
            resultado.add(List.of(dato.getMes(), dato.getClientesSinPedido(), dato.getClientesConUnPedido(), dato.getClientesRecurrentes()));
        }
        return resultado;
    }
}
