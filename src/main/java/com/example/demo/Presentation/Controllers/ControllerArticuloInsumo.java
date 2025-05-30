package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.ArticuloInsumo.ArregloActualizacionCostoDto;
import com.example.demo.Application.DTO.ArticuloInsumo.ArregloRecargaInsumoDto;
import com.example.demo.Application.DTO.ArticuloInsumo.InsumoDto;
import com.example.demo.Application.DTO.ArticuloInsumo.NuevoArticuloInsumoDto;
import com.example.demo.Domain.Service.ServiceArticuloInsumo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/insumo")
public class ControllerArticuloInsumo {
    private final ServiceArticuloInsumo serviceArticuloInsumo;

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

    //Lista todos los artículos insumo
    @GetMapping("/lista")
    public List<InsumoDto> listaInsumos(){
        return serviceArticuloInsumo.listaInsumos();
    }

    //Devuelve el nombre de un insumo
    @GetMapping("/obtenerNombre/{idArticuloInsumo}")
    public String obtenerNombre(@PathVariable Long idArticuloInsumo){
        return serviceArticuloInsumo.obtenerNombreInsumo(idArticuloInsumo);
    }
}
