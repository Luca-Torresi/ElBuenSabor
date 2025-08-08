package com.example.demo.Application.Mapper;

import com.example.demo.Application.DTO.Promocion.DetallePromocionDto;
import com.example.demo.Application.DTO.Promocion.NuevoDetallePromocionDto;
import com.example.demo.Domain.Entities.Articulo;
import com.example.demo.Domain.Entities.DetallePromocion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DetallePromocionMapper {

    @Mapping(source = "idArticulo", target = "articulo")
    DetallePromocion detallePromocionDtoToDetallePromocion(NuevoDetallePromocionDto nuevoDetallePromocionDto);

    @Mapping(source = "articulo.idArticulo", target = "idArticulo")
    @Mapping(source = "articulo.nombre", target = "nombreArticulo")
    DetallePromocionDto detallePromocionToDetallePromocionDto(DetallePromocion detallePromocion);

    default Articulo map(Long idArticulo) {
        if (idArticulo == null) {
            return null;
        }
        Articulo art = new Articulo();
        art.setIdArticulo(idArticulo);
        return art;
    }
}
