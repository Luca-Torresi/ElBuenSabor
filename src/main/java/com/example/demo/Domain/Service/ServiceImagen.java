package com.example.demo.Domain.Service;

import com.example.demo.Domain.Entities.Articulo; // Importa Articulo
import com.example.demo.Domain.Entities.Imagen;
import com.example.demo.Domain.Entities.Usuario;
import com.example.demo.Domain.Repositories.RepoArticulo; // Importa RepoArticulo
import com.example.demo.Domain.Repositories.RepoImagen;
import com.example.demo.Domain.Repositories.RepoUsuario;
import com.example.demo.Domain.Service.Cloudinary.ServiceCloudinary;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceImagen {
    private ServiceCloudinary serviceCloudinary;
    private RepoImagen repoImagen;
    private RepoUsuario repoUsuario;
    private RepoArticulo repoArticulo;

    public ResponseEntity<List<Map<String, Object>>> getAllImages() {
        try {
            List<Imagen> imagenes = repoImagen.findAll();
            List<Map<String, Object>> imageList = imagenes.stream()
                    // CAMBIO AQUÍ: Castear los valores a (Object) explícitamente
                    // O hacer un Map.of(K, V) donde V es Object
                    .map(imagen -> Map.<String, Object>of(
                            "id", imagen.getId(),
                            "publicId", imagen.getPublicId(),
                            "name", imagen.getName(),
                            "url", imagen.getUrl()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(imageList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Sube una imagen a Cloudinary y la guarda en la tabla 'Imagen' sin asociarla a un usuario/artículo específico.
     * Útil para galerías o cuando la asociación se hace en un paso posterior.
     */
    @Transactional
    public ResponseEntity<Map<String, Object>> uploadGenericImage(MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("status", (Object)"ERROR", "message", (Object)"El archivo no puede estar vacío.")); // CAST A OBJECT
        }
        try {
            Map<String, String> uploadResult = serviceCloudinary.uploadFile(file);
            String imageUrl = uploadResult.get("url");
            String publicId = uploadResult.get("publicId");

            Imagen imagen = Imagen.builder()
                    .publicId(publicId)
                    .name(file.getOriginalFilename())
                    .url(imageUrl)
                    .build();
            imagen = repoImagen.save(imagen);

            return ResponseEntity.status(HttpStatus.OK).body(Map.<String, Object>of( // CAST A OBJECT
                    "status", "OK",
                    "message", "Imagen cargada genéricamente.",
                    "id", imagen.getId().toString(),
                    "publicId", imagen.getPublicId(),
                    "url", imagen.getUrl()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", (Object)"ERROR", "message", (Object)("Ocurrió un error inesperado al cargar la imagen: " + e.getMessage()))); // CAST A OBJECT
        }
    }

    /**
     * Elimina una imagen genérica por su publicId y su UUID de la DB.
     * Útil para eliminar imágenes de la tabla 'Imagen' que no estén directamente asociadas o para limpieza.
     */
    @Transactional
    public ResponseEntity<String> deleteGenericImage(String publicId, UUID uuid) {
        try {
            Optional<Imagen> imagenOptional = repoImagen.findById(uuid);
            if (imagenOptional.isEmpty() || !imagenOptional.get().getPublicId().equals(publicId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"status\":\"ERROR\", \"message\":\"Imagen no encontrada con ese ID o publicId no coincide.\"}");
            }

            Imagen imagenAEliminar = imagenOptional.get();

            // Desasociar si está asociada a algún Articulo o Usuario (siempre una buena práctica)
            // Aunque esta es una imagen "genérica", la desasociación preventiva es buena.
            // Para Articulo:
            repoArticulo.findByImagen(imagenAEliminar).ifPresent(articulo -> {
                articulo.setImagen(null);
                repoArticulo.save(articulo);
            });
            // Para Usuario:
            repoUsuario.findByImagen(imagenAEliminar).ifPresent(usuario -> {
                usuario.setImagen(null);
                repoUsuario.save(usuario);
            });


            serviceCloudinary.deleteImage(publicId); // Eliminar de Cloudinary
            repoImagen.delete(imagenAEliminar); // Eliminar de la base de datos local

            return ResponseEntity.status(HttpStatus.OK).body("{\"status\":\"OK\", \"message\":\"Imagen eliminada correctamente.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }


    // --- Métodos específicos para Imágenes de Perfil (mantienen la lógica anterior) ---
    @Transactional
    public ResponseEntity<Map<String, String>> uploadProfileImage(MultipartFile file, Long idUsuario) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("status", "ERROR", "message", "No se ha seleccionado ningún archivo."));
        }
        try {
            Optional<Usuario> usuarioOptional = repoUsuario.findById(idUsuario);
            if (usuarioOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "ERROR", "message", "Usuario no encontrado."));
            }
            Usuario usuario = usuarioOptional.get();

            Imagen imagenExistente = usuario.getImagen();
            if (imagenExistente != null) {
                usuario.setImagen(null);
                repoUsuario.save(usuario);
                serviceCloudinary.deleteImage(imagenExistente.getPublicId());
                repoImagen.delete(imagenExistente);
            }

            Map<String, String> uploadResult = serviceCloudinary.uploadFile(file);
            String imageUrl = uploadResult.get("url");
            String publicId = uploadResult.get("publicId");

            if (imageUrl == null || imageUrl.isEmpty() || publicId == null || publicId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "ERROR", "message", "No se pudo obtener la URL o Public ID de la imagen desde Cloudinary."));
            }

            Imagen nuevaImagen = Imagen.builder()
                    .publicId(publicId)
                    .name(file.getOriginalFilename())
                    .url(imageUrl)
                    .build();
            nuevaImagen = repoImagen.save(nuevaImagen);

            usuario.setImagen(nuevaImagen);
            repoUsuario.save(usuario);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "message", "Imagen de perfil cargada y asociada exitosamente.", "imageUrl", imageUrl, "publicId", publicId));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "ERROR", "message", "Ocurrió un error inesperado al cargar la imagen: " + e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<String> deleteProfileImage(Long idUsuario) {
        try {
            Optional<Usuario> usuarioOptional = repoUsuario.findById(idUsuario);
            if (usuarioOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"status\":\"ERROR\", \"message\":\"Usuario no encontrado.\"}");
            }
            Usuario usuario = usuarioOptional.get();

            Imagen imagenAEliminar = usuario.getImagen();
            if (imagenAEliminar == null) {
                return ResponseEntity.badRequest().body("{\"status\":\"ERROR\", \"message\":\"El usuario no tiene una imagen de perfil asignada.\"}");
            }

            usuario.setImagen(null);
            repoUsuario.save(usuario);

            serviceCloudinary.deleteImage(imagenAEliminar.getPublicId());
            repoImagen.delete(imagenAEliminar);

            return ResponseEntity.status(HttpStatus.OK).body("{\"status\":\"OK\", \"message\":\"Imagen de perfil eliminada exitosamente.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"status\":\"ERROR\", \"message\":\"Error al eliminar la imagen de perfil: " + e.getMessage() + "\"}");
        }
    }

    // --- Nuevos métodos específicos para Imágenes de Artículo ---
    @Transactional
    public ResponseEntity<Map<String, String>> uploadArticleImage(MultipartFile file, Long idArticulo) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("status", "ERROR", "message", "No se ha seleccionado ningún archivo."));
        }

        try {
            Optional<Articulo> articuloOptional = repoArticulo.findById(idArticulo);
            if (articuloOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", "ERROR", "message", "Artículo no encontrado."));
            }
            Articulo articulo = articuloOptional.get();

            Imagen imagenExistente = articulo.getImagen();
            if (imagenExistente != null) {
                articulo.setImagen(null);
                repoArticulo.save(articulo);
                serviceCloudinary.deleteImage(imagenExistente.getPublicId());
                repoImagen.delete(imagenExistente);
            }

            Map<String, String> uploadResult = serviceCloudinary.uploadFile(file);
            String imageUrl = uploadResult.get("url");
            String publicId = uploadResult.get("publicId");

            if (imageUrl == null || imageUrl.isEmpty() || publicId == null || publicId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", "ERROR", "message", "No se pudo obtener la URL o Public ID de la imagen desde Cloudinary."));
            }

            Imagen nuevaImagen = Imagen.builder()
                    .publicId(publicId)
                    .name(file.getOriginalFilename())
                    .url(imageUrl)
                    .build();
            nuevaImagen = repoImagen.save(nuevaImagen);

            articulo.setImagen(nuevaImagen);
            repoArticulo.save(articulo);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("status", "OK", "message", "Imagen de artículo cargada y asociada exitosamente.", "imageUrl", imageUrl, "publicId", publicId));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "ERROR", "message", "Ocurrió un error inesperado al cargar la imagen: " + e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<String> deleteArticleImage(Long idArticulo) {
        try {
            Optional<Articulo> articuloOptional = repoArticulo.findById(idArticulo);
            if (articuloOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"status\":\"ERROR\", \"message\":\"Artículo no encontrado.\"}");
            }
            Articulo articulo = articuloOptional.get();

            Imagen imagenAEliminar = articulo.getImagen();
            if (imagenAEliminar == null) {
                return ResponseEntity.badRequest().body("{\"status\":\"ERROR\", \"message\":\"El artículo no tiene una imagen asignada.\"}");
            }

            articulo.setImagen(null);
            repoArticulo.save(articulo);

            serviceCloudinary.deleteImage(imagenAEliminar.getPublicId());
            repoImagen.delete(imagenAEliminar);

            return ResponseEntity.status(HttpStatus.OK).body("{\"status\":\"OK\", \"message\":\"Imagen de artículo eliminada exitosamente.\" }");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"status\":\"ERROR\", \"message\":\"Error al eliminar la imagen del artículo: " + e.getMessage() + "\"}");
        }
    }
}