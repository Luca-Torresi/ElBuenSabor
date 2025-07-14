package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.RubroInsumo.ArregloRubroInsumoCompletoDto;
import com.example.demo.Application.DTO.RubroInsumo.ArregloRubroInsumoDto;
import com.example.demo.Application.DTO.RubroInsumo.NuevoRubroInsumoDto;
import com.example.demo.Domain.Entities.RubroInsumo;
import com.example.demo.Domain.Service.ServiceRubroInsumo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rubroInsumo")
public class ControllerRubroInsumo {
    private ServiceRubroInsumo serviceRubroInsumo;

    //Recibe los datos necesario para crear un nuevo rubro insumo
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    @PostMapping("/nuevo")
    public ResponseEntity<RubroInsumo> nuevoRubroInsumo(@RequestBody NuevoRubroInsumoDto nuevoRubroInsumoDto){
        RubroInsumo rubroInsumo = serviceRubroInsumo.nuevoRubro(nuevoRubroInsumoDto);
        return ResponseEntity.ok(rubroInsumo);
    }

    //Devuelve todos los rubros con sus detalles para ser mostrados en el ABM
    @GetMapping("/abm")
    public ResponseEntity<ArregloRubroInsumoCompletoDto> abmRubroInsumo(){
        ArregloRubroInsumoCompletoDto arreglo = serviceRubroInsumo.abmRubrosInsumo();
        return ResponseEntity.ok(arreglo);
    }

    //Devuelve una lista con los rubros
    @GetMapping("/lista")
    public ResponseEntity<ArregloRubroInsumoDto> listarRubros(){
        return ResponseEntity.ok(serviceRubroInsumo.listarRubrosInsumo());
    }
}
