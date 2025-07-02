package com.example.demo.Application.DTO.Direccion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ArregloDireccionDto {
    private List<DireccionDto> direcciones;
}
