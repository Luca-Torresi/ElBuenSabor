package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Categoria.CategoriaDto;
import com.example.demo.Application.DTO.Categoria.NuevaCategoriaDto;
import com.example.demo.Application.DTO.Categoria.NuevoMargenGananciaDto;
import com.example.demo.Application.DTO.Generic.AltaBajaDto;
import com.example.demo.Domain.Entities.Categoria;
import com.example.demo.Domain.Service.ServiceCategoria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categoria")
public class ControllerCategoria {
    private final ServiceCategoria serviceCategoria;

    //Recibe la información necesaria para la creación de una nueva categoría
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping("/nueva")
    public ResponseEntity<Categoria> nuevaCategoria(@RequestBody NuevaCategoriaDto nuevaCategoriaDto){
        Categoria categoria = serviceCategoria.cargarNuevaCategoria(nuevaCategoriaDto);

        return ResponseEntity.ok(categoria);
    }

    //Dar de alta o baja a una categoría
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping("/altaBaja/{idCategoria}")
    public ResponseEntity darDeAltaBajaLogica(@PathVariable Long idCategoria){
        serviceCategoria.darDeAltaBaja(idCategoria);

        return ResponseEntity.ok().build();
    }

    //Mostrar categorías en el catálogo
    @GetMapping("/lista")
    public List<CategoriaDto> listarCategorias(){
        return serviceCategoria.listarCategoriasCatalogo();
    }

    //Devuelve los datos de una categoría única
    @GetMapping("/obtenerNombre/{idCategoria}")
    public String buscarCategoria(@PathVariable Long idCategoria){
        return serviceCategoria.buscarCategoriaPorId(idCategoria);
    }

    @PutMapping("/actualizar/{idCategoria}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long idCategoria, @RequestBody NuevaCategoriaDto nuevaCategoriaDto) {
        Categoria actualizada = serviceCategoria.actualizarCategoria(idCategoria, nuevaCategoriaDto);
        return ResponseEntity.ok(actualizada);
    }


    /*
    //Recibe los datos necesarios para actualizar el margen de ganancia de una categorías
    @PostMapping("/modificarMargenGanancia")
    public void nuevoMargen(@RequestBody NuevoMargenGananciaDto nuevoMargenGananciaDto){
        serviceCategoria.modificarMargenGanancia(nuevoMargenGananciaDto);
    }
    */
}
