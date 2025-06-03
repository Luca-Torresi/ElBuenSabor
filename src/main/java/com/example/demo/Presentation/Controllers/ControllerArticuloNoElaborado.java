package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.ArticuloManufacturado.InformacionArticuloManufacturadoDto;
import com.example.demo.Application.DTO.ArticuloNoElaborado.InformacionArticuloNoElaboradoDto;
import com.example.demo.Domain.Service.ServiceArticuloNoElaborado;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articuloNoElaborado")
public class ControllerArticuloNoElaborado {
    private final ServiceArticuloNoElaborado serviceArticuloNoElaborado;

    //Lista los artículos en el ABM
    @GetMapping("/abm")
    public Page<InformacionArticuloNoElaboradoDto> articulosAbm(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "12") int size){
        return serviceArticuloNoElaborado.mostrarArticulosAbm(page, size);
    }
}
