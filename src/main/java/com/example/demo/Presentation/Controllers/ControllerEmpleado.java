package com.example.demo.Presentation.Controllers;

import com.example.demo.Domain.Service.ServiceEmpleado;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/empleado")
public class ControllerEmpleado {

    private final ServiceEmpleado serviceEmpleado;

    public ControllerEmpleado(ServiceEmpleado serviceEmpleado) {
        this.serviceEmpleado = serviceEmpleado;
    }
}
