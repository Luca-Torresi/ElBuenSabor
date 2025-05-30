package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Categoria.CategoriaDto;
import com.example.demo.Application.DTO.Categoria.NuevaCategoriaDto;
import com.example.demo.Application.DTO.Categoria.NuevoMargenGananciaDto;
import com.example.demo.Application.DTO.Generic.AltaBajaDto;
import com.example.demo.Domain.Service.ServiceCategoria;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categoria")
public class ControllerCategoria {
    private final ServiceCategoria serviceCategoria;

    //Recibe la información necesaria para la creación de una nueva categoría
    @PostMapping("/nueva")
    public void nuevaCategoria(@RequestBody NuevaCategoriaDto nuevaCategoriaDto){
        serviceCategoria.cargarNuevaCategoria(nuevaCategoriaDto);
    }

    //Recibe los datos necesarios para actualizar el margen de ganancia de una categorías
    @PostMapping("/modificarMargenGanancia")
    public void nuevoMargen(@RequestBody NuevoMargenGananciaDto nuevoMargenGananciaDto){
        serviceCategoria.modificarMargenGanancia(nuevoMargenGananciaDto);
    }

    //Dar de alta o baja a una categoría
    @PostMapping("/altaBajaLogica")
    public void darDeAltaBajaLogica(@RequestBody AltaBajaDto altaBajaDto){
        serviceCategoria.darDeAltaBaja(altaBajaDto);
    }

    //Mostrar categorías en el catálogo
    @GetMapping("/lista")
    public List<CategoriaDto> listarCategorias(){
        return serviceCategoria.listarCategoriasCatalogo();
    }

    //Devuelve los datos de una categoría única
    @GetMapping("/nombre/{idCategoria}")
    public CategoriaDto buscarCategoria(@PathVariable Long idCategoria){
        return serviceCategoria.buscarCategoriaPorId(idCategoria);
    }
}
