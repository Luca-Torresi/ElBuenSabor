package com.example.demo.Application.DTO.Usuario;

import com.example.demo.Application.DTO.Generic.ImagenModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Recibe la información del cliente cuando este se registra
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class RegistroClienteDto {
    private String nombre;
    private String apellido;
    private String telefono;
    private ImagenModel imagenModel;
    private DireccionDto direccionDto;
}
