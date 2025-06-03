package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.ArticuloManufacturado.ArticuloManufacturadoDto;
import com.example.demo.Domain.Service.ServiceArticulo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articulo")
public class ControllerArticulo {
    private final ServiceArticulo serviceArticulo;

    //Devuelve los artículos para ser mostrados en el catálogo
    @GetMapping("/catalogo")
    public Page<ArticuloManufacturadoDto> mostrarArticulos(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "9") int size){
        return serviceArticuloManufacturado.listarArticulosCatalogo(page, size);
    }

    //Ejecuta el procedimiento almacenado de la base de datos el cual actualiza los precios de todos los artículos
    @PostMapping("/actualizarPrecios")
    public void actualizarPreciosArticulos(){
        serviceArticulo.actualizarPrecios();
    }
}
