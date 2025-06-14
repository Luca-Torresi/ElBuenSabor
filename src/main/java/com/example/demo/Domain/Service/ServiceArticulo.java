package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Articulo.ArticuloDto;
import com.example.demo.Application.Mapper.ArticuloMapper;
import com.example.demo.Domain.Entities.Articulo;
import com.example.demo.Domain.Entities.ArticuloNoElaborado;
import com.example.demo.Domain.Repositories.RepoArticulo;
import com.example.demo.Domain.Repositories.RepoArticuloNoElaborado;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ServiceArticulo {
    private final RepoArticulo repoArticulo;
    private final ArticuloMapper articuloMapper;
    private final EntityManager entityManager;
    private final RepoArticuloNoElaborado repoArticuloNoElaborado;

    //Devuelve una lista con todos los artículos para ser mostrados en el catálogo
    public Page<ArticuloDto> listarArticulosCatalogo(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Articulo> paginaArticulos = repoArticulo.findByFechaBajaIsNull(pageable);

        return paginaArticulos.map(articulo -> {
            ArticuloDto dto = articuloMapper.articuloToArticuloDto(articulo);
            if(articulo.isEsManufacturado()){
                dto.setPuedeElaborarse(repoArticulo.sePuedeElaborar(articulo.getIdArticulo()));
            } else{
                ArticuloNoElaborado noElaborado = repoArticuloNoElaborado.findById(articulo.getIdArticulo()).get();
                dto.setPuedeElaborarse(noElaborado.getStock() >= 1 ? true : false);
            }
            return dto;
        });
    }

    //Actualiza los precios de todos los artículos manufacturados
    @Transactional
    public void actualizarPrecios(){
        entityManager.createNativeQuery("CALL actualizarPreciosArticulos()")
                .executeUpdate();
    }

    //Dar de alta o baja a un artículo
    public void darDeAltaBaja(Long idArticulo) {
        Articulo articulo = repoArticulo.findById(idArticulo).get();
        articulo.setFechaBaja(
                articulo.getFechaBaja() != null ? null : LocalDate.now()
        );
        repoArticulo.save(articulo);
    }
}
