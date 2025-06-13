package com.example.demo.Domain.Service.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.Resource;
import org.cloudinary.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ServiceCloudinaryImpl implements ServiceCloudinary {
    @Resource
    private Cloudinary cloudinary;
    @Override
    public String uploadFile( MultipartFile file) {
        try{
            HashMap<Object, Object> options = new HashMap<>();
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
            String publicId = (String) uploadedFile.get("public_id");
            return cloudinary.url().secure(true).generate(publicId);
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }}
    @Override
    public ResponseEntity<String> deleteImage(String publicId, UUID idBd) {
        try {
            // Eliminar la imagen en Cloudinary
            Map response = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            JSONObject json = new JSONObject(response);
            // Verificar si la eliminación fue exitosa
            if ("ok".equals(json.getString("result"))) {
                return new ResponseEntity<>("{\"status\":\"OK\", \"message\":\"Image deleted successfully.\"}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("{\"status\":\"ERROR\", \"message\":\"Failed to delete image.\"}", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
        }
    }
}

