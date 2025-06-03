package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Articulo.ArticuloDto;
import com.example.demo.Application.Mapper.ArticuloMapper;
import com.example.demo.Domain.Entities.Articulo;
import com.example.demo.Domain.Repositories.RepoArticulo;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceArticulo {
    private final EntityManager entityManager;
    private final RepoArticulo repoArticulo;
    private final ArticuloMapper articuloMapper;

    //Devuelve una lista con todos los artículos para ser mostrados en el catálogo
    public Page<ArticuloDto> listarArticulosCatalogo(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Articulo> paginaArticulos = repoArticulo.findByFechaBajaIsNull(pageable);

        return paginaArticulos.map(articulo -> {
            ArticuloDto dto = articuloMapper.articuloToArticuloDto(articulo);
            boolean puedeElaborarse = repoArticulo.sePuedeElaborar(articulo.getIdArticulo());
            dto.setPuedeElaborarse(puedeElaborarse);
            return dto;
        });
    }

    //Actualiza los precios de todos los artículos manufacturados
    @Transactional
    public void actualizarPrecios(){
        entityManager.createNativeQuery("CALL actualizarPreciosArticulos()")
                .executeUpdate();
    }
}
