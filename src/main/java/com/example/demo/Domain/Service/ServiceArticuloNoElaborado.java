package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.ArticuloNoElaborado.InformacionArticuloNoElaboradoDto;
import com.example.demo.Application.DTO.ArticuloNoElaborado.NuevoArticuloNoElaboradoDto;
import com.example.demo.Application.DTO.Generic.AltaBajaDto;
import com.example.demo.Application.Mapper.ImagenMapper;
import com.example.demo.Application.Mapper.NoElaboradoMapper;
import com.example.demo.Domain.Entities.*;
import com.example.demo.Domain.Repositories.RepoArticuloNoElaborado;
import com.example.demo.Domain.Repositories.RepoCategoria;
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
public class ServiceArticuloNoElaborado {
    private final RepoArticuloNoElaborado repoArticuloNoElaborado;
    private final NoElaboradoMapper noElaboradoMapper;
    private final RepoCategoria repoCategoria;
    private final ImagenMapper imagenMapper;
    private final EntityManager entityManager;

    //Persiste en la base de datos un nuevo artículo manufacturado
    @Transactional
    public void nuevoArticulo(NuevoArticuloNoElaboradoDto nuevoArticuloDto) {
        Categoria categoria = repoCategoria.findById(nuevoArticuloDto.getIdCategoria()).get();

        ArticuloNoElaborado articulo = noElaboradoMapper.nuevoArticuloNoElaboradoDtoToArticuloNoElaborado(nuevoArticuloDto);
        articulo.setCategoria(categoria);
        articulo.setFechaBaja(nuevoArticuloDto.isDadoDeAlta() ? null : LocalDate.now());

        articulo = repoArticuloNoElaborado.save(articulo);

        if(articulo.getPrecioVenta() == 0){
            entityManager.createNativeQuery("CALL modificarPrecioVenta(:_idArticulo)")
                    .setParameter("_idArticulo", articulo.getIdArticulo())
                    .executeUpdate();
        }
    }

    //Dar de alta o baja a un artículo
    public void darDeAltaBaja(AltaBajaDto altaBajaDto) {
        ArticuloNoElaborado articuloNoElaborado = repoArticuloNoElaborado.findById(altaBajaDto.getId()).get();

        articuloNoElaborado.setFechaBaja(altaBajaDto.isDadoDeAlta() ? null : LocalDate.now());
        repoArticuloNoElaborado.save(articuloNoElaborado);
    }

    @Transactional
    public void actualizarArticulo(Long id, InformacionArticuloNoElaboradoDto dto) {
        ArticuloNoElaborado articulo = repoArticuloNoElaborado.findById(id).get();

        articulo.setNombre(dto.getNombre());
        articulo.setDescripcion(dto.getDescripcion());

        Categoria categoria = repoCategoria.findById(dto.getIdCategoria()).get();
        articulo.setCategoria(categoria);

        if (dto.getImagenDto() != null) {
            ImagenNoElaborado imagenNoElaborado = imagenMapper.imagenDtoToImagenNoElaborado(dto.getImagenDto());
            articulo.setImagen(imagenNoElaborado);
        }

        articulo = repoArticuloNoElaborado.save(articulo);

        if(!dto.isPrecioModificado()){
            entityManager.createNativeQuery("CALL modificarPrecioVenta(:_idManufacturado)")
                    .setParameter("_idManufacturado", articulo.getIdArticulo())
                    .executeUpdate();
        }
    }

    //Devuelve las páginas con la información de los artículos manufacturados para el ABM
    public Page<InformacionArticuloNoElaboradoDto> mostrarArticulosAbm(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticuloNoElaborado> paginaArticulosAbm = repoArticuloNoElaborado.findAll(pageable);

        return paginaArticulosAbm.map(articulo -> {
            InformacionArticuloNoElaboradoDto dto = noElaboradoMapper.articuloNoElaboradoToDto(articulo);
            dto.setDadoDeAlta(articulo.getFechaBaja() != null ? false : true);
            return dto;
        });
    }
}
