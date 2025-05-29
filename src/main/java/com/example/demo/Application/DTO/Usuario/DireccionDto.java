package com.example.demo.Application.DTO.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Utilizado para el registro de un nuevo cliente, como para la modificación de su dirección
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class DireccionDto {
    private String calle;
    private String numero;
    private String piso;
    private String dpto;
    private Long idDepartamento;
}
