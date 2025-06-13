package com.example.demo.Application.DTO.Generic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ImagenModel {
    private String nombre;
    private MultipartFile file;
}
