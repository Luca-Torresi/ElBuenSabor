package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Generic.ImagenModel;
import com.example.demo.Application.DTO.Promocion.*;
import com.example.demo.Application.Mapper.ImagenMapperImpl;
import com.example.demo.Application.Mapper.PromocionMapper;
import com.example.demo.Domain.Entities.Articulo;
import com.example.demo.Domain.Entities.ArticuloManufacturado;
import com.example.demo.Domain.Entities.Imagen;
import com.example.demo.Domain.Entities.Promocion;
import com.example.demo.Domain.Exceptions.ArticuloDadoDeBajaException;
import com.example.demo.Domain.Repositories.RepoArticulo;
import com.example.demo.Domain.Repositories.RepoArticuloManufacturado;
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
    private final RepoArticulo repoArticulo;

    //Cargar nueva promoción
    public Long nuevaPromocion(NuevaPromocionDto nuevaPromocionDto) {
        Promocion promocion = promocionMapper.promocionDtoToPromocion(nuevaPromocionDto);

        Articulo articulo = repoArticulo.findById(nuevaPromocionDto.getIdArticulo()).get();
        promocion.setArticulo(articulo);

        promocion = repoPromocion.save(promocion);
        return promocion.getIdPromocion();
    }

    //Obtiene de la base de datos todas las promociones activas para ser mostradas en el catálogo
    public List<PromocionCatalogoDto> promocionesCatalogo() {
        List<Promocion> promociones = repoPromocion.findByActivoTrue();

        List<PromocionCatalogoDto> lista = new ArrayList<>();
        for(Promocion promocion : promociones) {
            PromocionCatalogoDto dto = promocionMapper.promocionToPromocionCatalogoDto(promocion);
            lista.add(dto);
        }
        return lista;
    }

    //Obtiene de la base de datos todas las promociones para ser mostradas en el ABM
    public List<PromocionAbmDto> promocionesAbm(){
        List<Promocion> promociones = repoPromocion.findAll();

        List<PromocionAbmDto> lista = new ArrayList<>();
        for(Promocion promocion : promociones) {
            PromocionAbmDto dto = promocionMapper.promocionToPromocionAbmDto(promocion);
            lista.add(dto);
        }
        return lista;
    }

    //Modifica las datos de una promoción
    public Promocion modificarPromocion(Long idPromocion, NuevaPromocionDto dto) {
        Promocion promocion = repoPromocion.findById(idPromocion).get();
        Articulo articulo = repoArticulo.findById(dto.getIdArticulo()).get();

        promocion.setTitulo(dto.getTitulo());
        promocion.setDescripcion(dto.getDescripcion());
        promocion.setDescuento(dto.getDescuento());
        promocion.setActivo(dto.isActivo());
        promocion.setHorarioInicio(dto.getHorarioInicio());
        promocion.setHorarioFin(dto.getHorarioFin());
        promocion.setArticulo(articulo);

        return repoPromocion.save(promocion);
    }

    //Dar de alta o baja una promoción
    public void darDeAltaBajaPromocion(Long idPromocion){
        Promocion promocion = repoPromocion.findById(idPromocion).get();

        Articulo articulo = promocion.getArticulo();
        if(articulo.getFechaBaja() ==  null){
            promocion.setActivo(promocion.isActivo() ? false : true);
        } else {
            throw new ArticuloDadoDeBajaException("El artículo correspondiente a esta promoción se encuentra dado de baja");
        }

        repoPromocion.save(promocion);
    }
}
