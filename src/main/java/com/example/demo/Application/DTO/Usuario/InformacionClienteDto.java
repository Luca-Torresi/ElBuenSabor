package com.example.demo.Application.DTO.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class InformacionClienteDto {
    private Long idUsuario;
    private String nombreYApellido;
    private String email;
    private String telefono;
    private Integer cantidadPedidos;
}
