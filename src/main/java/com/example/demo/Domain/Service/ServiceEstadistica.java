package com.example.demo.Domain.Service;

import com.example.demo.Domain.Projections.ColumnChartProjection;
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
    public List<List<Object>> pieChart(){
        List<List<Object>> resultado = new ArrayList<>();

        resultado.add(List.of("Artículo", "Cantidad de ventas"));

        List<PieChartProjection> datos = repoEstadistica.obtenerArticulosMasVendidos();
        for (PieChartProjection dato : datos) {
            resultado.add(List.of(dato.getNombre(), dato.getCantVendido()));
        }
        return resultado;
    }

    //Obtiene la información de la VIEW 'promedioVentasPorHora' para mostrar estos datos en un gráfico
    public List<List<Object>> columnChart(){
        List<List<Object>> resultado = new ArrayList<>();

        resultado.add(List.of("Hora", "Porcentaje de ventas"));

        List<ColumnChartProjection> datos = repoEstadistica.obtenerPromedioVentasPorHora();
        for (ColumnChartProjection dato : datos) {
            resultado.add(List.of(dato.getHora(), dato.getPorcentaje()));
        }
        return resultado;
    }
}
