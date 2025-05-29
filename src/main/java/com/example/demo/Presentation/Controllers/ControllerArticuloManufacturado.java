package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.ArticuloManufacturado.ArticuloManufacturadoDto;
import com.example.demo.Application.DTO.ArticuloManufacturado.InformacionArticuloManufacturadoDto;
import com.example.demo.Application.DTO.ArticuloManufacturado.ManufacturadoAbmDto;
import com.example.demo.Application.DTO.ArticuloManufacturado.NuevoArticuloManufacturadoDto;
import com.example.demo.Application.DTO.Generic.AltaBajaDto;
import com.example.demo.Domain.Service.ServiceArticuloManufacturado;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articuloManufacturado")
public class ControllerArticuloManufacturado {
    private final ServiceArticuloManufacturado serviceArticuloManufacturado;

    //Recibe los datos necesarios para crear un nuevo artículo
    @PostMapping("/nuevo")
    public void nuevoArticuloManufacturado(@RequestBody NuevoArticuloManufacturadoDto nuevoArticuloManufacturadoDto) {
        serviceArticuloManufacturado.nuevoArticulo(nuevoArticuloManufacturadoDto);
    }

    //Dar de alta o baja un artículo
    @PostMapping("/altaBajaLogica")
    public void darDeAltaBajaLogica(@RequestBody AltaBajaDto altaBajaDto){
        serviceArticuloManufacturado.darDeAltaBaja(altaBajaDto);
    }

    //Devuelve los artículos para ser mostrados en el catálogo
    @GetMapping("/catalogo")
    public Page<ArticuloManufacturadoDto> mostrarArticulos(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "9") int size){
        return serviceArticuloManufacturado.listarArticulosCatalogo(page, size);
    }

    //Lista los artículos manufacturados en el ABM
    @GetMapping("/abm")
    public Page<ManufacturadoAbmDto> articulosAbm(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "12") int size){
        return serviceArticuloManufacturado.mostrarArticulosAbm(page, size);
    }

    //Devuelve los detalles del artículo indicado cuando se hace click en 'ver' o 'modificar' en el ABM
    @GetMapping("/detalles/{idArticuloManufacturado}")
    public ResponseEntity<InformacionArticuloManufacturadoDto> obtenerDetallesArticulo(@PathVariable Long idArticuloManufacturado) {
        InformacionArticuloManufacturadoDto dto = serviceArticuloManufacturado.obtenerInformacionArticulo(idArticuloManufacturado);
        return ResponseEntity.ok(dto);
    }

    //Recibe los datos necesarios para la modificación de un artículo manufacturado
    @PutMapping("/admin/articulos/{id}")
    public ResponseEntity<Void> actualizarArticulo(@PathVariable Long id, @RequestBody InformacionArticuloManufacturadoDto dto) {
        serviceArticuloManufacturado.actualizarArticulo(id, dto);
        return ResponseEntity.noContent().build();
    }
}
