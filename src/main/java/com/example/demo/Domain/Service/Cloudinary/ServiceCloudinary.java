package com.example.demo.Domain.Service.Cloudinary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

public interface ServiceCloudinary {
    public default String uploadFile(MultipartFile file) {
        return null;
    }
    public ResponseEntity<String> deleteImage(String publicId, UUID uuid);
}
