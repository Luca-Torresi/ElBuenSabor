package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Promocion.*;
import com.example.demo.Domain.Entities.Articulo;
import com.example.demo.Domain.Entities.Promocion;
import com.example.demo.Domain.Service.ServiceImagen;
import com.example.demo.Domain.Service.ServicePromocion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/promocion")
public class ControllerPromocion {
    private final ServicePromocion servicePromocion;
    private final ServiceImagen serviceImagen;

    //Recibe los datos necesarios para la creación de una nueva promoción
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    @PostMapping(value = "/nueva", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> crearNuevaPromocion(
            @RequestPart("promocion") String promocionJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "url", required = false) String url) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        NuevaPromocionDto dto = mapper.readValue(promocionJson, NuevaPromocionDto.class);
        Long idPromocion = servicePromocion.nuevaPromocion(dto);

        if (file != null && !file.isEmpty()) {
            serviceImagen.subirImagenArchivoPromocion(file, idPromocion);
        } else if (url != null && !url.isBlank()) {
            serviceImagen.subirImagenUrlPromocion(url, idPromocion);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Devuelve las promociones para ser mostradas en el catálogo
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'CLIENTE')")
    @GetMapping("/catalogo")
    public ResponseEntity<List<PromocionCatalogoDto>> promocionesCatalogo(){
        List<PromocionCatalogoDto> lista = servicePromocion.promocionesCatalogo();
        return ResponseEntity.ok(lista);
    }

    //Devuelve las promociones para ser mostradas en el ABM
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    @GetMapping("/abm")
    public ResponseEntity<List<PromocionAbmDto>> promocionesAbm() {
        try {
            List<PromocionAbmDto> lista = servicePromocion.promocionesAbm();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Recibe los datos necesarios para modificar una promoción existente
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    @PutMapping(value = "/modificar/{idPromocion}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Promocion> modificarPromocion(
            @PathVariable Long idPromocion,
            @RequestPart("promocion") String promocionJson,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "url", required = false) String url) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        NuevaPromocionDto dto = mapper.readValue(promocionJson, NuevaPromocionDto.class);

        Promocion promocion = servicePromocion.modificarPromocion(idPromocion, dto);

        if (file != null && !file.isEmpty()) {
            serviceImagen.subirImagenArchivoPromocion(file, idPromocion);
        } else if (url != null && !url.isBlank()) {
            serviceImagen.subirImagenUrlPromocion(url, idPromocion);
        }

        return ResponseEntity.ok(promocion);
    }

    //Endpoint para dar de alta o baja a las promociones
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    @PutMapping("/altaBaja/{idPromocion}")
    public ResponseEntity darDeAltaBajaPromocion(@PathVariable Long idPromocion){
        servicePromocion.darDeAltaBajaPromocion(idPromocion);
        return ResponseEntity.ok().build();
    }

    @GetMapping("detalle/{idPromocion}")
    public ResponseEntity<Promocion> estadoPromocion(@PathVariable Long idPromocion) {
        Promocion promocion = servicePromocion.obtenerPromocionByID(idPromocion);
        return ResponseEntity.ok(promocion);
    }

    // Calcula el precio sugerido de una promoción en base a los artículos seleccionados
    @PostMapping("/precio-sugerido")
    public ResponseEntity<Double> calcularPrecioSugerido(
            @RequestBody List<NuevoDetallePromocionDto> detalles) {
        Double precio = servicePromocion.calcularPrecioSugerido(detalles);
        return ResponseEntity.ok(precio);
    }

    //Devuelve el resumen de una promoción específica
    @GetMapping("/{idPromocion}/resumen")
    public ResponseEntity<PromocionResumenDto> resumenPromocion(@PathVariable Long idPromocion) {
        PromocionResumenDto resumen = servicePromocion.calcularResumenPromocion(idPromocion);
        return ResponseEntity.ok(resumen);
    }
}
