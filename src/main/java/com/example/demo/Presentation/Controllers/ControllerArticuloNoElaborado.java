package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.ArticuloNoElaborado.InformacionArticuloNoElaboradoDto;
import com.example.demo.Application.DTO.ArticuloNoElaborado.NuevoArticuloNoElaboradoDto;
import com.example.demo.Domain.Entities.ArticuloNoElaborado;
import com.example.demo.Domain.Service.ServiceArticuloNoElaborado;
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
@RequestMapping("/articuloNoElaborado")
public class ControllerArticuloNoElaborado {
    private final ServiceArticuloNoElaborado serviceArticuloNoElaborado;
    private final ServiceImagen serviceImagen;

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping(value = "/nuevo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArticuloNoElaborado> nuevoArticuloNoElaborado(
            @RequestParam("articulo") String articuloJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {

        NuevoArticuloNoElaboradoDto dto = new ObjectMapper().readValue(articuloJson, NuevoArticuloNoElaboradoDto.class);
        ArticuloNoElaborado articuloNoElaborado = serviceArticuloNoElaborado.nuevoArticulo(dto);

        if (file != null && !file.isEmpty()) {
            serviceImagen.uploadArticleImage(file, articuloNoElaborado.getIdArticulo());
        } else if (dto.getImagenUrl() != null && !dto.getImagenUrl().isBlank()) {
            serviceImagen.saveImageUrl(dto.getImagenUrl(), articuloNoElaborado.getIdArticulo());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(articuloNoElaborado);
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PutMapping(value = "/modificar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> actualizarArticulo(
            @PathVariable Long id,
            @RequestParam("articulo") String informacionArticuloNoElaboradoDtoJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {

        InformacionArticuloNoElaboradoDto dto = new ObjectMapper().readValue(informacionArticuloNoElaboradoDtoJson, InformacionArticuloNoElaboradoDto.class);
        serviceArticuloNoElaborado.actualizarArticulo(id, dto);

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
    public Page<InformacionArticuloNoElaboradoDto> articulosAbm(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return serviceArticuloNoElaborado.mostrarArticulosAbm(page, size);
    }
}