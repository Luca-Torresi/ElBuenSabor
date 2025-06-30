// src/main/java/com/example/demo/Domain/Service/ServiceImagen.java
package com.example.demo.Domain.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ServiceImagen {
    ResponseEntity<List<Map<String, Object>>> getAllImages();
    // Este método es para subir imágenes que NO están asociadas inmediatamente a un usuario/artículo.
    // Podría ser usado por un admin para subir imágenes a una "galería" y luego asignarlas.
    // O podrías eliminarlo si todas las subidas siempre se asocian.
    ResponseEntity<Map<String, Object>> uploadGenericImage(MultipartFile file); // Retorna map con publicId y url de la imagen subida.

    // Método para eliminar una imagen genérica (usando publicId y el UUID de tu DB)
    // Esto es útil si tienes una galería donde el frontend sabe ambos IDs.
    ResponseEntity<String> deleteGenericImage(String publicId, UUID uuid);

    // --- Métodos específicos para Imágenes de Perfil (asociadas a un usuario) ---
    ResponseEntity<Map<String, String>> uploadProfileImage(MultipartFile file, Long idUsuario);
    ResponseEntity<String> deleteProfileImage(Long idUsuario);

    // --- Métodos específicos para Imágenes de Artículo (asociadas a un artículo) ---
    // (Opcional, podrías manejar esto directamente en ServiceArticulo)
    ResponseEntity<Map<String, String>> uploadArticleImage(MultipartFile file, Long idArticulo);
    ResponseEntity<String> deleteArticleImage(Long idArticulo);
}