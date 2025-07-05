package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.ArticuloManufacturado.InformacionArticuloManufacturadoDto;
import com.example.demo.Application.DTO.ArticuloManufacturado.NuevoArticuloManufacturadoDto;
import com.example.demo.Domain.Entities.ArticuloManufacturado;
import com.example.demo.Domain.Service.ServiceArticuloManufacturado;
import com.example.demo.Domain.Service.ServiceImagen;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping("/articuloManufacturado")
public class ControllerArticuloManufacturado {
    private final ServiceArticuloManufacturado serviceArticuloManufacturado;
    private final ServiceImagen serviceImagen;

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping(value = "/nuevo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArticuloManufacturado> nuevoArticuloManufacturado(
            @RequestParam("articulo") String articuloJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {

        NuevoArticuloManufacturadoDto dto = new ObjectMapper().readValue(articuloJson, NuevoArticuloManufacturadoDto.class);
        ArticuloManufacturado articuloManufacturado = serviceArticuloManufacturado.nuevoArticulo(dto);

        if (file != null && !file.isEmpty()) {
            serviceImagen.uploadArticleImage(file, articuloManufacturado.getIdArticulo());
        } else if (dto.getImagenUrl() != null && !dto.getImagenUrl().isBlank()) {
            serviceImagen.saveImageUrl(dto.getImagenUrl(), articuloManufacturado.getIdArticulo());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(articuloManufacturado);
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PutMapping(value = "/modificar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> actualizarArticulo(
            @PathVariable Long id,
            @RequestParam("articulo") String informacionArticuloDtoJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {

        // 1. Actualizar los datos del artículo
        InformacionArticuloManufacturadoDto dto = new ObjectMapper().readValue(informacionArticuloDtoJson, InformacionArticuloManufacturadoDto.class);
        serviceArticuloManufacturado.actualizarArticulo(id, dto);

        // 2. Manejo de imagen
        if (file != null && !file.isEmpty()) {
            serviceImagen.uploadArticleImage(file, id);
        } else if (dto.getImagenUrl() != null && !dto.getImagenUrl().isBlank()) {
            serviceImagen.saveImageUrl(dto.getImagenUrl(), id);
        } else {
            serviceImagen.deleteArticleImage(id);
        }

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/abm")
    public Page<InformacionArticuloManufacturadoDto> articulosAbm(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return serviceArticuloManufacturado.mostrarArticulosAbm(page, size);
    }
}