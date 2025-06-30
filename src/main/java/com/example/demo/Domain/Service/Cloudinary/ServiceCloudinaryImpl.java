package com.example.demo.Domain.Service.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.Resource;
// Importaciones que ya no necesitas si solo verificas "ok" o "not found"
// import org.cloudinary.json.JSONObject;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects; // Para Objects.requireNonNull

@Service
public class ServiceCloudinaryImpl implements ServiceCloudinary {
    @Resource
    private Cloudinary cloudinary;

    @Override
    public Map<String, String> uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío para subirlo a Cloudinary.");
        }
        try {
            // Sube el archivo a Cloudinary. ObjectUtils.emptyMap() para opciones predeterminadas.
            // Si necesitas public_id personalizado, podrías pasarlo aquí,
            // por ejemplo: Map<Object, Object> options = ObjectUtils.asMap("public_id", "mi_id_personalizado");
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            // Extrae el public_id y la URL segura (HTTPS) de la respuesta de Cloudinary.
            // Usamos Objects.requireNonNull para asegurar que no son nulos y lanzar un error claro si lo fueran.
            String publicId = (String) Objects.requireNonNull(uploadResult.get("public_id"), "Cloudinary public_id es nulo al subir.");
            String url = (String) Objects.requireNonNull(uploadResult.get("secure_url"), "Cloudinary secure_url es nula al subir.");

            // Retorna un mapa con ambos valores.
            Map<String, String> result = new HashMap<>();
            result.put("publicId", publicId);
            result.put("url", url);
            return result;

        } catch (IOException e) {
            // Registra la excepción y lanza una RuntimeException para un manejo de errores más centralizado.
            e.printStackTrace();
            throw new RuntimeException("Error de E/S al subir archivo a Cloudinary: " + e.getMessage(), e);
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada de Cloudinary.
            e.printStackTrace();
            throw new RuntimeException("Error inesperado al subir archivo a Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteImage(String publicId) {
        if (publicId == null || publicId.isEmpty()) {
            throw new IllegalArgumentException("El publicId no puede ser nulo o vacío para eliminar la imagen de Cloudinary.");
        }
        try {
            // Intenta destruir (eliminar) la imagen en Cloudinary usando su publicId.
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String responseResult = (String) result.get("result");

            // Cloudinary devuelve "ok" si la eliminación fue exitosa y "not found" si la imagen ya no existe.
            // Ambos casos son "aceptables" para nosotros en términos de que la imagen ya no está en Cloudinary.
            if (!"ok".equals(responseResult) && !"not found".equals(responseResult)) {
                // Si la respuesta no es "ok" ni "not found", algo salió mal y lanzamos una excepción.
                throw new IOException("Fallo al eliminar la imagen en Cloudinary: " + result.toString());
            }
        } catch (IOException e) {
            // Registra y relanza la excepción para que los servicios de nivel superior puedan manejarla.
            e.printStackTrace();
            throw new RuntimeException("Error de E/S al comunicarse con Cloudinary para eliminar imagen: " + e.getMessage(), e);
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada de Cloudinary.
            e.printStackTrace();
            throw new RuntimeException("Error inesperado al eliminar imagen de Cloudinary: " + e.getMessage(), e);
        }
    }
}