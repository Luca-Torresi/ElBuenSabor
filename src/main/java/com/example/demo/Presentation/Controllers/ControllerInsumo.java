package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Insumo.ArregloActualizacionCostoDto;
import com.example.demo.Domain.Service.ServiceInsumo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/insumo")
public class ControllerInsumo {

    private ServiceInsumo serviceInsumo;

    public ControllerInsumo(ServiceInsumo serviceInsumo){
        this.serviceInsumo = serviceInsumo;
    }

    @PostMapping("/actualizarCostos")
    public void actualizarCostos(@RequestBody ArregloActualizacionCostoDto arregloActualizacionCostoDto){
        serviceInsumo.actualizarCostos(arregloActualizacionCostoDto);
    }

}
