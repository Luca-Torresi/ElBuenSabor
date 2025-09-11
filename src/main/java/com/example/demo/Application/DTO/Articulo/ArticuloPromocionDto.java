package com.example.demo.Application.DTO.Articulo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticuloPromocionDto {
    private Long idArticulo;
    private String nombre;
    private Double precioVenta;
    private Boolean activo;
    private Boolean puedeElaborarse;
    private Boolean disponible; // activo && puedeElaborarse
}
