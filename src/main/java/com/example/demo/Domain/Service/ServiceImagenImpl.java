package com.example.demo.Domain.Service;

import com.example.demo.Domain.Entities.Imagen;
import com.example.demo.Domain.Repositories.RepoImagen;
import com.example.demo.Domain.Service.Cloudinary.ServiceCloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.List;

@Service
public class ServiceImagenImpl implements ServiceImagen {

    @Autowired
    private ServiceCloudinary serviceCloudinary;
    @Autowired
    private RepoImagen repoImagen;

    @Override
    public ResponseEntity<List<Map<String, Object>>> getAllImages() {
        try {
            List<Imagen> imagenes = repoImagen.findAll();
            List<Map<String, Object>> imageList = new ArrayList<>();

            for (Imagen imagen : imagenes) {
                Map<String, Object> imageMap = new HashMap<>();
                imageMap.put("id", imagen.getId());
                imageMap.put("name", imagen.getName());
                imageMap.put("url", imagen.getUrl());
                imageList.add(imageMap);
            }

            return ResponseEntity.ok(imageList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @Override
    public ResponseEntity<String> uploadImages(MultipartFile[] files) {
        List<String> urls = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                if (file.getName().isEmpty()) {
                    return ResponseEntity.badRequest().build();
                }

                Imagen imagen = new Imagen();
                imagen.setName(file.getName());
                imagen.setUrl(serviceCloudinary.uploadFile(file));
                if (imagen.getUrl() == null) {
                    return ResponseEntity.badRequest().build();
                }

                // Agregar la URL a la lista de URLs
                urls.add(imagen.getUrl());
                repoImagen.save(imagen);
            };

            // Convertir la lista de URLs a un array de strings y devolver como JSON
            return new ResponseEntity<>("{\"status\":\"OK\", \"urls\":" + urls + "}", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }
    @Override
    public ResponseEntity<String> deleteImage(String publicId, UUID idBd) {
        try {
            repoImagen.deleteById(idBd);
            return serviceCloudinary.deleteImage(publicId, idBd);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }
}
