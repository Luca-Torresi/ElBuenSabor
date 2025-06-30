package com.example.demo.Presentation.Controllers;

import com.example.demo.Domain.Service.ServiceImagen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin("*") // Considera especificar tus orígenes en producción
@RequestMapping("/images")
public class ControllerImagen {

    @Autowired
    private ServiceImagen serviceImagen;

    // Puedes mantener este endpoint si necesitas subir una sola imagen de forma genérica
    // (e.g., para luego asociarla a un artículo o un usuario manualmente)
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadGenericImage(
            @RequestParam("file") MultipartFile file) {
        try {
            // Llama al nuevo método para subir una imagen genérica
            return serviceImagen.uploadGenericImage(file);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            // Las RuntimeExceptions lanzadas por ServiceCloudinary o ServiceImagenImpl
            // se pueden capturar aquí para devolver un error 500
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al subir la imagen: " + e.getMessage()));
        }
    }

    // Este endpoint es para eliminar una imagen genérica, usando su publicId de Cloudinary y su UUID de tu DB
    @DeleteMapping("/delete") // Cambiado a DELETE y URL más descriptiva
    public ResponseEntity<String> deleteGenericImage(
            @RequestParam(value = "publicId", required = true) String publicId,
            @RequestParam(value = "uuid", required = true) String uuidString) {
        try {
            UUID uuid = UUID.fromString(uuidString);
            // Llama al nuevo método para eliminar una imagen genérica
            return serviceImagen.deleteGenericImage(publicId, uuid);
        } catch (IllegalArgumentException e) {
            // Si el UUID no es válido
            e.printStackTrace();
            return ResponseEntity.badRequest().body("{\"error\": \"Formato de UUID inválido: " + e.getMessage() + "\"}");
        } catch (RuntimeException e) {
            // Captura las RuntimeExceptions del servicio
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Error al eliminar la imagen: " + e.getMessage() + "\"}");
        }
    }

    // TRAE TODAS LAS IMAGENES EN LA TABLA IMAGENES
    @GetMapping("/all") // URL más descriptiva
    public ResponseEntity<List<Map<String, Object>>> getAllImages() {
        try {
            return serviceImagen.getAllImages();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /*
     * Consideraciones adicionales:
     *
     * 1. Si tu aplicación **siempre** asocia una imagen a un usuario o artículo
     * en el momento de la subida, entonces podrías **eliminar** los
     * endpoints `/upload` y `/delete` de este controlador.
     * En su lugar, los endpoints de `ControllerCliente` y `ControllerArticulo`
     * serían los únicos puntos de entrada para la carga de imágenes.
     *
     * 2. Si mantienes estos endpoints, considera aplicar seguridad a ellos
     * (por ejemplo, `@PreAuthorize("hasAuthority('ADMINISTRADOR')")`)
     * para que solo ciertos roles puedan subir o eliminar imágenes de forma genérica.
     */
}