package com.example.demo.Presentation.Controllers;

import com.example.demo.Domain.Service.ServiceEstadistica;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/estadisticas")
public class ControllerEstadistica {
    private final ServiceEstadistica serviceEstadistica;

    //Devuelve los datos necesarios para generar el gráfico de torta con los artículos más vendidos y sus cantidades
    @GetMapping("/articulosMasVendidos")
    public List<List<Object>> pieChart(@RequestParam String categoria){
        return serviceEstadistica.pieChart(categoria);
    }

    //Devuelve los datos necesarios para generar el gráfico de columnas con las horas y cantidad de ventas
    @GetMapping("/promedioPedidosPorHora")
    public List<List<Object>> columnChart(){
        return serviceEstadistica.ColumnChart();
    }

    //Devuelve los datos necesarios para generar el gráfico de columnas con los meses y el total recaudado
    @GetMapping("/recaudadoPorMes")
    public List<List<Object>> recaudadoPorMes(){
        return serviceEstadistica.LineChart();
    }

    //Devuelve los datos necesarios para generar el gráfico de varias líneas para ver el comportamiento de los clientes
    @GetMapping("/frecuenciaClientes")
    public List<List<Object>> frecuenciaClientes(){
        return serviceEstadistica.multipleLineChart();
    }
}
