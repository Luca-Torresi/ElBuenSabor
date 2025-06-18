package com.example.demo.Presentation.Controllers;


import com.example.demo.Application.DTO.Departamento.DepartamentoDto;
import com.example.demo.Domain.Service.DepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @GetMapping("/mendoza")
    public List<DepartamentoDto> obtenerDepartamentosMendoza() {
        return departamentoService.obtenerDepartamentosDeMendoza();
    }
}
