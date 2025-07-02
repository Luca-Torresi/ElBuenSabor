package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Articulo.ArticuloDto;
import com.example.demo.Domain.Service.ServiceArticulo;
import com.example.demo.Domain.Service.ServiceImagen;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articulo")
public class ControllerArticulo {
    private final ServiceArticulo serviceArticulo;
    private final ServiceImagen serviceImagen;

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
    public ResponseEntity<String> actualizarPreciosArticulos(){
        serviceArticulo.actualizarPrecios();
        return ResponseEntity.ok("Se actualizó correctamente el precio de todos los artículos");
    }


    //Endpoint para cargar la imagen de un artículo existente. Requiere el ID del artículo como PathVariable.
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'COCINERO')")
    @PostMapping(value = "/{id}/imagen/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadArticleImage(
            @PathVariable("id") Long idArticulo,
            @RequestParam("file") MultipartFile file) {
        return serviceImagen.uploadArticleImage(file, idArticulo);
    }

    //Endpoint para eliminar la imagen de un artículo existente. Requiere el ID del artículo como PathVariable.
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'COCINERO')")
    @DeleteMapping("/{id}/imagen/delete")
    public ResponseEntity<String> deleteArticleImage(@PathVariable("id") Long idArticulo) {
        return serviceImagen.deleteArticleImage(idArticulo);
    }
}
