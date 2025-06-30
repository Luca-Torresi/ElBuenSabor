package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.ArticuloNoElaborado.InformacionArticuloNoElaboradoDto;
import com.example.demo.Application.DTO.ArticuloNoElaborado.NuevoArticuloNoElaboradoDto;
import com.example.demo.Domain.Entities.ArticuloNoElaborado;
import com.example.demo.Domain.Service.ServiceArticuloNoElaborado;
import com.example.demo.Domain.Service.ServiceImagen;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articuloNoElaborado")
public class ControllerArticuloNoElaborado {
    private final ServiceArticuloNoElaborado serviceArticuloNoElaborado;
    private final ServiceImagen serviceImagen;

    //Recibe los datos necesarios para crear un nuevo artículo
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping(value = "/nuevo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArticuloNoElaborado> nuevoArticuloManufacturado(
            @RequestPart("articulo") NuevoArticuloNoElaboradoDto nuevoArticuloNoElaboradoDto,
            @RequestParam(value = "file", required = false)MultipartFile file) {
        ArticuloNoElaborado articuloNoElaborado = serviceArticuloNoElaborado.nuevoArticulo(nuevoArticuloNoElaboradoDto);

        // 2. Si se proporcionó un archivo, cárgalo y asócialo al artículo
        if (file != null && !file.isEmpty()) {
            serviceImagen.uploadArticleImage(file, articuloNoElaborado.getIdArticulo());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(articuloNoElaborado);
    }

    //Recibe los datos necesarios para la modificación de un artículo no elaborado
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PutMapping(value = "/modificar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // <--- ¡Importante!
    public ResponseEntity<Void> actualizarArticulo(
            @PathVariable Long id,
            @RequestPart("articulo") InformacionArticuloNoElaboradoDto dto, // <--- Cambiado a @RequestPart
            @RequestParam(value = "file", required = false) MultipartFile file) { // <--- Nuevo parámetro para la imagen

        // 1. Actualizar los datos del artículo (sin imagen)
        serviceArticuloNoElaborado.actualizarArticulo(id, dto);

        // 2. Lógica para la imagen:
        if (file != null && !file.isEmpty()) {
            serviceImagen.uploadArticleImage(file, id);
        } else if (dto.getImagenUrl() == null || dto.getImagenUrl().isEmpty()) {
            serviceImagen.deleteArticleImage(id);
        }

        return ResponseEntity.noContent().build();
    }

    //Lista los artículos no elaborados en el ABM
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/abm")
    public Page<InformacionArticuloNoElaboradoDto> articulosAbm(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "12") int size){
        return serviceArticuloNoElaborado.mostrarArticulosAbm(page, size);
    }
}
