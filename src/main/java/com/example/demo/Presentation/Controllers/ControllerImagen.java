package com.example.demo.Presentation.Controllers;

import com.example.demo.Domain.Service.ServiceImagen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/images")
public class ControllerImagen {

    @Autowired
    private ServiceImagen serviceImagen;

    @PostMapping("/uploads")
    public ResponseEntity<String> uploadImages(
            @RequestParam(value = "uploads", required = true) MultipartFile[] files) {
        try {
            return serviceImagen.uploadImages(files);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/deleteImg")
    public ResponseEntity<String> deleteById(
            @RequestParam(value = "publicId", required = true) String publicId,
            @RequestParam(value = "uuid", required = true) String uuidString) {
        try {
            UUID uuid = UUID.fromString(uuidString);
            return serviceImagen.deleteImage(publicId, uuid);
        } catch (IllegalArgumentException e) {
            // UUID.fromString lanzará una IllegalArgumentException si la cadena no es un UUID válido
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid UUID format");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred");
        }
    }

    //TRAE TODAS LAS IMAGENES EN LA TABAL IMAGENES
    @GetMapping("/getImages")
    public ResponseEntity<List<Map<String, Object>>> getAll() {
        try {
            return  serviceImagen.getAllImages();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    };
}

