package com.example.demo.Application.DTO.Promocion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NuevoDetallePromocionDto {
    private Long idArticulo;
    private Integer cantidad;
}
