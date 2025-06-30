package com.example.demo.Application.DTO.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelefonoUpdateDto {

    @NotBlank(message = "El número de teléfono no puede estar vacío.")
    private String telefono;
}