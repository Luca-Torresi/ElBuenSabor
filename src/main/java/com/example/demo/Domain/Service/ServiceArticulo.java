package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Articulo.ArticuloDto;
import com.example.demo.Application.Mapper.ArticuloMapper;
import com.example.demo.Domain.Entities.Articulo;
import com.example.demo.Domain.Entities.ArticuloNoElaborado;
import com.example.demo.Domain.Repositories.RepoArticulo;
import com.example.demo.Domain.Repositories.RepoArticuloNoElaborado;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceArticulo {
    private final RepoArticulo repoArticulo;
    private final ArticuloMapper articuloMapper;
    private final EntityManager entityManager;
    private final RepoArticuloNoElaborado repoArticuloNoElaborado;

    @Transactional(readOnly = true)
    public Page<ArticuloDto> listarArticulosCatalogo(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Articulo> paginaArticulos = repoArticulo.findByFechaBajaIsNull(pageable);

        return paginaArticulos.map(articulo -> {
            ArticuloDto dto = articuloMapper.articuloToArticuloDto(articulo);

            if (articulo.isEsManufacturado()) {
                dto.setPuedeElaborarse(repoArticulo.sePuedeElaborar(articulo.getIdArticulo()));
            } else {
                Optional<ArticuloNoElaborado> optional = repoArticuloNoElaborado.findById(articulo.getIdArticulo());
                boolean puedeElaborarse = optional.map(noElaborado -> noElaborado.getStock() >= 1).orElse(false);
                dto.setPuedeElaborarse(puedeElaborarse);
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
    @Transactional
    public void darDeAltaBaja(Long idArticulo) {
        Articulo articulo = repoArticulo.findById(idArticulo).get();
        articulo.setFechaBaja(
                articulo.getFechaBaja() != null ? null : LocalDate.now()
        );
        repoArticulo.save(articulo);
    }
}
