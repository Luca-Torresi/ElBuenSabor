package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.RubroInsumo.RubroInsumoDto;
import com.example.demo.Domain.Service.ServiceRubroInsumo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rubroInsumo")
public class ControllerRubroInsumo {
    private ServiceRubroInsumo serviceRubroInsumo;

    //Recibe los datos necesario para crear un nuevo rubro insumo
    @PostMapping("/nuevo")
    public void nuevoRubroInsumo(@RequestBody RubroInsumoDto rubroInsumoDto){
        serviceRubroInsumo.nuevoRubro(rubroInsumoDto);
    }
}
