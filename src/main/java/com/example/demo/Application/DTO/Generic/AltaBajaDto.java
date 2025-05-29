package com.example.demo.Application.DTO.Generic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Para dar de alta o baja, ya sea un empleado, artículo manufacturado o categoría
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class AltaBajaDto {
    private long id;
    private boolean dadoDeAlta;
}
