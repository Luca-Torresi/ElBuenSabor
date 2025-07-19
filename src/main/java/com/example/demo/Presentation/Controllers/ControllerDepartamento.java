package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Direccion.DepartamentoDto;
import com.example.demo.Domain.Service.ServiceDepartamento;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ControllerDepartamento {
    private final ServiceDepartamento serviceDepartamento;

    @GetMapping("/mendoza")
    public List<DepartamentoDto> obtenerDepartamentosMendoza() {
        return serviceDepartamento.obtenerDepartamentosDeMendoza();
    }
}