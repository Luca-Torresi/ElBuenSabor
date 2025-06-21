package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Articulo.ArticuloDto;
import com.example.demo.Domain.Service.ServiceArticulo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articulo")
public class ControllerArticulo {
    private final ServiceArticulo serviceArticulo;

    //Devuelve los artículos para ser mostrados en el catálogo
    @GetMapping("/catalogo")
    public Page<ArticuloDto> mostrarArticulosCatalogo(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "9") int size){
        return serviceArticulo.listarArticulosCatalogo(page, size);
    }

    //Dar de alta o baja un artículo
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping("/altaBaja/{idArticulo}")
    public ResponseEntity darDeAltaBajaLogica(@PathVariable Long idArticulo) {
        serviceArticulo.darDeAltaBaja(idArticulo);

        return ResponseEntity.ok().build();
    }

    //Ejecuta el procedimiento almacenado de la base de datos el cual actualiza los precios de todos los artículos
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping("/actualizarPrecios")
    public void actualizarPreciosArticulos(){
        serviceArticulo.actualizarPrecios();
    }
}
