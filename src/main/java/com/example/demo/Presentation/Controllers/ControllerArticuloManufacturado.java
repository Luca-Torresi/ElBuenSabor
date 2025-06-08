package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.ArticuloManufacturado.InformacionArticuloManufacturadoDto;
import com.example.demo.Application.DTO.ArticuloManufacturado.NuevoArticuloManufacturadoDto;
import com.example.demo.Application.DTO.Generic.AltaBajaDto;
import com.example.demo.Domain.Entities.Articulo;
import com.example.demo.Domain.Entities.ArticuloManufacturado;
import com.example.demo.Domain.Service.ServiceArticuloManufacturado;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articuloManufacturado")
public class ControllerArticuloManufacturado {
    private final ServiceArticuloManufacturado serviceArticuloManufacturado;

    //Recibe los datos necesarios para la creación de un nuevo artículo manufacturado
    @PostMapping("/nuevo")
    public ResponseEntity<ArticuloManufacturado> nuevoArticuloManufacturado(@RequestBody NuevoArticuloManufacturadoDto nuevoArticuloManufacturadoDto) {
        ArticuloManufacturado articuloManufacturado = serviceArticuloManufacturado.nuevoArticulo(nuevoArticuloManufacturadoDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(articuloManufacturado);
    }

    //Lista los artículos en el ABM
    @GetMapping("/abm")
    public Page<InformacionArticuloManufacturadoDto> articulosAbm(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "12") int size){
        return serviceArticuloManufacturado.mostrarArticulosAbm(page, size);
    }

    //Recibe los datos necesarios para la modificación de un artículo manufacturado
    @PutMapping("/modificar/{id}")
    public ResponseEntity<Void> actualizarArticulo(@PathVariable Long id, @RequestBody InformacionArticuloManufacturadoDto dto) {
        serviceArticuloManufacturado.actualizarArticulo(id, dto);

        return ResponseEntity.noContent().build();
    }
}
