package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.ArticuloManufacturado.InformacionArticuloManufacturadoDto;
import com.example.demo.Application.DTO.ArticuloManufacturado.NuevoArticuloManufacturadoDto;
import com.example.demo.Domain.Entities.ArticuloManufacturado;
import com.example.demo.Domain.Service.ServiceArticuloManufacturado;
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
@RequestMapping("/articuloManufacturado")
public class ControllerArticuloManufacturado {
    private final ServiceArticuloManufacturado serviceArticuloManufacturado;
    private final ServiceImagen serviceImagen;

    //Recibe los datos necesarios para la creación de un nuevo artículo manufacturado
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping(value = "/nuevo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArticuloManufacturado> nuevoArticuloManufacturado(
            @RequestPart("articulo") NuevoArticuloManufacturadoDto nuevoArticuloManufacturadoDto,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        ArticuloManufacturado articuloManufacturado = serviceArticuloManufacturado.nuevoArticulo(nuevoArticuloManufacturadoDto);

        // 2. Si se proporcionó un archivo, cárgalo y asócialo al artículo recién creado
        if (file != null && !file.isEmpty()) {
            // Llama a tu ServiceImagen para subir y asociar la imagen.
            // Tu serviceImagen.uploadArticleImage ya maneja la eliminación de la anterior si existe.
            serviceImagen.uploadArticleImage(file, articuloManufacturado.getIdArticulo());
            // Nota: Si quieres que el 'articuloManufacturado' devuelto en la respuesta
            // tenga la entidad Imagen asociada actualizada, podrías hacer un
            // repoArticuloManufacturado.findById(articuloManufacturado.getIdArticulo()).get()
            // o que serviceImagen.uploadArticleImage devuelva la Imagen y la setees aquí.
            // Para simplificar, la entidad ya se actualizó en la DB por ServiceImagen.
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(articuloManufacturado);
    }

    //Lista los artículos en el ABM
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/abm")
    public Page<InformacionArticuloManufacturadoDto> articulosAbm(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "12") int size){
        return serviceArticuloManufacturado.mostrarArticulosAbm(page, size);
    }

    //Recibe los datos necesarios para la modificación de un artículo manufacturado
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PutMapping(value = "/modificar/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> actualizarArticulo(
            @PathVariable Long id,
            @RequestPart("articulo") InformacionArticuloManufacturadoDto dto, // <--- DATOS JSON como @RequestPart
            @RequestParam(value = "file", required = false) MultipartFile file) { // <--- NUEVO ARCHIVO como @RequestParam (opcional)

        // 1. Actualizar los datos del artículo (sin tocar la imagen todavía)
        serviceArticuloManufacturado.actualizarArticulo(id, dto);

        // 2. Lógica para la imagen:
        if (file != null && !file.isEmpty()) {
            // Si se envió un nuevo archivo, subirlo. Esto también reemplaza/elimina el anterior.
            serviceImagen.uploadArticleImage(file, id);
        } else if (dto.getImagenUrl() == null || dto.getImagenUrl().isEmpty()) {
            // Si NO se envió un nuevo archivo Y el DTO indica que la URL de imagen es nula/vacía,
            // significa que el frontend quiere eliminar la imagen existente.
            serviceImagen.deleteArticleImage(id);
        }
        // Si 'file' es nulo y 'dto.getImagenUrl()' no es nulo/vacío,
        // significa que la imagen existente se mantiene (no se hace nada).

        return ResponseEntity.noContent().build();
    }
}
