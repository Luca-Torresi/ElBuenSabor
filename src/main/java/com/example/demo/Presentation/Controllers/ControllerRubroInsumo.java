package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.RubroInsumo.ArregloRubroInsumoCompletoDto;
import com.example.demo.Application.DTO.RubroInsumo.ArregloRubroInsumoDto;
import com.example.demo.Application.DTO.RubroInsumo.NuevoRubroInsumoDto;
import com.example.demo.Domain.Entities.RubroInsumo;
import com.example.demo.Domain.Service.ServiceRubroInsumo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rubroInsumo")
public class ControllerRubroInsumo {
    private ServiceRubroInsumo serviceRubroInsumo;

    public ControllerRubroInsumo(ServiceRubroInsumo serviceRubroInsumo) {
        this.serviceRubroInsumo = serviceRubroInsumo;
    }

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

    //Dar de alta o baja a un rubro insumo
    @PostMapping("/altaBaja/{idRubroInsumo}")
    public ResponseEntity darDeAltaBajaLogica(@PathVariable Long idRubroInsumo){
        serviceRubroInsumo.darDeAltaBaja(idRubroInsumo);
        return ResponseEntity.ok().build();
    }

    //Devuelve una lista con los rubros
    @GetMapping("/lista")
    public ResponseEntity<ArregloRubroInsumoDto> listarRubros(){
        return ResponseEntity.ok(serviceRubroInsumo.listarRubrosInsumo());
    }
}
