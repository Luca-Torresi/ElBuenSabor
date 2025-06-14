package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Generic.AltaBajaDto;
import com.example.demo.Application.DTO.Usuario.NuevoEmpleadoDto;
import com.example.demo.Domain.Service.ServiceEmpleado;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/empleado")
public class ControllerEmpleado {
    private ServiceEmpleado serviceEmpleado;

    //El administrador cargar en el sistema un nuevo empleado
    @PostMapping("/nuevo")
    public void nuevoEmpleado(@RequestBody NuevoEmpleadoDto nuevoEmpleadoDto) {
        serviceEmpleado.cargarNuevoEmpleado(nuevoEmpleadoDto);
    }

    //Recibe la información para dar de alta o baja a un empleado
    @PostMapping("/altaBaja")
    public void altaBajaLogica(@PathVariable Long idEmpleado) {
        serviceEmpleado.altaBajaEmpleado(idEmpleado);
    }
}
