package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Promocion.PromocionDto;
import com.example.demo.Application.Mapper.PromocionMapper;
import com.example.demo.Domain.Entities.ArticuloManufacturado;
import com.example.demo.Domain.Entities.Promocion;
import com.example.demo.Domain.Repositories.RepoArticuloManufacturado;
import com.example.demo.Domain.Repositories.RepoPromocion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServicePromocion {
    private final RepoPromocion repoPromocion;
    private final PromocionMapper promocionMapper;
    private final RepoArticuloManufacturado repoArticuloManufacturado;

    //Cargar nueva promoción
    public void nuevaPromocion(PromocionDto promocionDto) {
        Promocion promocion = promocionMapper.promocionDtoToPromocion(promocionDto);

        ArticuloManufacturado articuloManufacturado = repoArticuloManufacturado.findById(promocionDto.getIdArticuloManufacturado()).get();
        promocion.setArticulo(articuloManufacturado);

        repoPromocion.save(promocion);
    }
}
