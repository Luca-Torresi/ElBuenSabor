package com.example.demo.Domain.Service.Cloudinary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

public interface ServiceCloudinary {
    Map<String, String> uploadFile(MultipartFile file);
    void deleteImage(String publicId);
}
