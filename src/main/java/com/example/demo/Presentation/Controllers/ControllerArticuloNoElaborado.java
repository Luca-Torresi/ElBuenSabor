package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.ArticuloNoElaborado.InformacionArticuloNoElaboradoDto;
import com.example.demo.Application.DTO.ArticuloNoElaborado.NuevoArticuloNoElaboradoDto;
import com.example.demo.Domain.Service.ServiceArticuloNoElaborado;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articuloNoElaborado")
public class ControllerArticuloNoElaborado {
    private final ServiceArticuloNoElaborado serviceArticuloNoElaborado;

    //Recibe los datos necesarios para crear un nuevo artículo
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/nuevo")
    public void nuevoArticuloManufacturado(@RequestBody NuevoArticuloNoElaboradoDto nuevoArticuloNoElaboradoDto) {
        serviceArticuloNoElaborado.nuevoArticulo(nuevoArticuloNoElaboradoDto);
    }

    //Recibe los datos necesarios para la modificación de un artículo no elaborado
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/modificar/{id}")
    public ResponseEntity<Void> actualizarArticulo(@PathVariable Long id, @RequestBody InformacionArticuloNoElaboradoDto dto) {
        serviceArticuloNoElaborado.actualizarArticulo(id, dto);
        return ResponseEntity.noContent().build();
    }

    //Lista los artículos no elaborados en el ABM
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/abm")
    public Page<InformacionArticuloNoElaboradoDto> articulosAbm(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "12") int size){
        return serviceArticuloNoElaborado.mostrarArticulosAbm(page, size);
    }
}
