package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.ArticuloInsumo.*;
import com.example.demo.Domain.Entities.ArticuloInsumo;
import com.example.demo.Domain.Service.ServiceArticuloInsumo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/insumo")
public class ControllerArticuloInsumo {
    private final ServiceArticuloInsumo serviceArticuloInsumo;

    //Recibe los detalles para la creación de un nuevo insumo
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping("/nuevo")
    public ResponseEntity<ArticuloInsumo> nuevoArticuloInsumo(@RequestBody NuevoInsumoDto nuevoInsumoDto){
        ArticuloInsumo articuloInsumo = serviceArticuloInsumo.cargarNuevoInsumo(nuevoInsumoDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(articuloInsumo);
    }

    //Recibe un arreglo con todos los insumos a recargar
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping("/recargaStock")
    public ResponseEntity ingresoInsumos(@RequestBody ArregloRecargaInsumoDto arregloRecargaInsumoDto){
        serviceArticuloInsumo.recargaDeInsumos(arregloRecargaInsumoDto);

        return ResponseEntity.ok().build();
    }

    //Lista todos los artículos insumo
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/lista")
    public ResponseEntity<ArregloInsumoDto> listaInsumos(){
        return ResponseEntity.ok(serviceArticuloInsumo.listaInsumos());
    }

    //Devuelve la información de los artículos insumos para ser mostrados en el ABM
    @GetMapping("/abm")
    public Page<InformacionInsumoDto> abmArticulosInsumo(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "12") int size){
        return serviceArticuloInsumo.listarArticulosABM(page, size);
    }

    //Recibe un DTO con la información necesaria para modificar los datos de un artículo insumo
    @PutMapping("/modificar/{idArticuloInsumo}")
    public ResponseEntity<String> modificarArticuloInsumo(@PathVariable Long idArticuloInsumo, @RequestBody InsumoModificacionDto insumoModificacionDto){
        serviceArticuloInsumo.modificarArticuloInsumo(idArticuloInsumo, insumoModificacionDto);
        return ResponseEntity.ok("El insumo se modificó exitosamente");
    }

    //Dar de alta o baja a un artículo insumo
    @PostMapping("/altaBaja/{idArticuloInsumo}")
    public ResponseEntity darDeAltaBajaLogica(@PathVariable Long idArticuloInsumo){
        serviceArticuloInsumo.darDeAltaBaja(idArticuloInsumo);

        return ResponseEntity.ok().build();
    }

    //Devuelve, para el insumo seleccionado, los nombres de los artículos que contienen este insumo y la cantidad
    @GetMapping("/articulos/{idArticuloInsumo}")
    public ResponseEntity<ArregloIngredienteDto> obtenerUsosInsumo(@PathVariable Long idArticuloInsumo){
        return ResponseEntity.ok(serviceArticuloInsumo.obtenerUsosInsumo(idArticuloInsumo));
    }
}
