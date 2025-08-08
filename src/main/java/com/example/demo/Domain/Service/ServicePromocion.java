package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Promocion.*;
import com.example.demo.Application.Mapper.PromocionMapper;
import com.example.demo.Domain.Entities.Articulo;
import com.example.demo.Domain.Entities.DetallePromocion;
import com.example.demo.Domain.Entities.Promocion;
import com.example.demo.Domain.Exceptions.ArticuloDadoDeBajaException;
import com.example.demo.Domain.Repositories.RepoArticulo;
import com.example.demo.Domain.Repositories.RepoDetallePromocion;
import com.example.demo.Domain.Repositories.RepoPromocion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePromocion {
    private final RepoPromocion repoPromocion;
    private final PromocionMapper promocionMapper;
    private final RepoDetallePromocion repoDetallePromocion;
    private final RepoArticulo repoArticulo;

    //Cargar nueva promoción
    public Long nuevaPromocion(NuevaPromocionDto nuevaPromocionDto) {
        Promocion promocion = promocionMapper.promocionDtoToPromocion(nuevaPromocionDto);

        promocion = repoPromocion.save(promocion);
        return promocion.getIdPromocion();
    }

    //Obtiene de la base de datos todas las promociones activas para ser mostradas en el catálogo
    public List<PromocionCatalogoDto> promocionesCatalogo() {
        return repoPromocion.findByActivoTrue()
                .stream()
                .map(promocionMapper::promocionToPromocionCatalogoDto)
                .toList();
    }

    //Obtiene de la base de datos todas las promociones para ser mostradas en el ABM
    public List<PromocionAbmDto> promocionesAbm(){
        return repoPromocion.findAll()
                .stream()
                .map(promocionMapper::promocionToPromocionAbmDto)
                .toList();
    }

    //Modifica las datos de una promoción
    public Promocion modificarPromocion(Long idPromocion, NuevaPromocionDto dto) {
        Promocion promocion = repoPromocion.findById(idPromocion).get();
        promocionMapper.updateFromDto(dto, promocion);

        return repoPromocion.save(promocion);
    }

    //Dar de alta o baja una promoción
    public void darDeAltaBajaPromocion(Long idPromocion){
        Promocion promocion = repoPromocion.findById(idPromocion).get();
        List<DetallePromocion> detalles = repoDetallePromocion.findByPromocionId(idPromocion);

        for (DetallePromocion detalle : detalles) {
            if(detalle.getArticulo().getFechaBaja() != null){
                throw new ArticuloDadoDeBajaException("El artículo correspondiente a esta promoción se encuentra dado de baja");
            }
        }

        promocion.setActivo(
                promocion.getActivo() == true ? false : true
        );
        repoPromocion.save(promocion);
    }
}
