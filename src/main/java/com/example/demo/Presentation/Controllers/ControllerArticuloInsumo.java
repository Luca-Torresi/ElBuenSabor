package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.ArticuloInsumo.ArregloActualizacionCostoDto;
import com.example.demo.Application.DTO.ArticuloInsumo.ArregloRecargaInsumoDto;
import com.example.demo.Application.DTO.ArticuloInsumo.NuevoArticuloInsumoDto;
import com.example.demo.Domain.Service.ServiceArticuloInsumo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/insumo")
public class ControllerArticuloInsumo {
    private ServiceArticuloInsumo serviceArticuloInsumo;

    //Recibe los detalles del nuevo insumo
    @PostMapping("/nuevo")
    public void nuevoInsumo(@RequestBody NuevoArticuloInsumoDto nuevoArticuloInsumoDto){
        serviceArticuloInsumo.cargarNuevoInsumo(nuevoArticuloInsumoDto);
    }

    //Recibe un arreglo con los nuevos costos de algunos insumos
    @PostMapping("/actualizarCostos")
    public void actualizarCostos(@RequestBody ArregloActualizacionCostoDto arregloActualizacionCostoDto){
        serviceArticuloInsumo.actualizarCostos(arregloActualizacionCostoDto);
    }

    //Recibe un arreglo con todos los insumos recargados
    @PostMapping("/ingresoProveedor")
    public void ingresoInsumos(@RequestBody ArregloRecargaInsumoDto arregloRecargaInsumoDto){
        serviceArticuloInsumo.recargaDeInsumos(arregloRecargaInsumoDto);
    }
}
