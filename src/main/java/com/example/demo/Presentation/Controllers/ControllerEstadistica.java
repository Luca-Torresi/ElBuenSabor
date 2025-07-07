package com.example.demo.Presentation.Controllers;

import com.example.demo.Domain.Service.ServiceEstadistica;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/estadisticas")
public class ControllerEstadistica {
    private final ServiceEstadistica serviceEstadistica;

    //Devuelve los datos necesarios para generar el gráfico de torta con los artículos más vendidos y sus cantidades
    @GetMapping("/articulosMasVendidos")
    public List<List<Object>> pieChart(){
        return serviceEstadistica.pieChart();
    }

    //Devuelve los datos necesarios para generar el gráfico de columnas con las horas y cantidad de ventas
    @GetMapping("/promedioVentasPorHora")
    public List<List<Object>> columnChart(){
        return serviceEstadistica.columnChart();
    }
}
