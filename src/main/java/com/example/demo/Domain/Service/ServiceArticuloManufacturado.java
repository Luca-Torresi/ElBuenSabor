package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.ArticuloManufacturado.*;
import com.example.demo.Application.DTO.Generic.AltaBajaDto;
import com.example.demo.Application.Mapper.ImagenMapper;
import com.example.demo.Application.Mapper.ManufacturadoMapper;
import com.example.demo.Domain.Entities.*;
import com.example.demo.Domain.Repositories.RepoArticuloInsumo;
import com.example.demo.Domain.Repositories.RepoArticuloManufacturado;
import com.example.demo.Domain.Repositories.RepoCategoria;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceArticuloManufacturado {
    private final RepoArticuloManufacturado repoArticuloManufacturado;
    private final RepoCategoria repoCategoria;
    private final ManufacturadoMapper manufacturadoMapper;
    private final RepoArticuloInsumo repoArticuloInsumo;
    private final ImagenMapper imagenMapper;
    private final EntityManager entityManager;

    //Persiste en la base de datos un nuevo artículo manufacturado
    @Transactional
    public void nuevoArticulo(NuevoArticuloManufacturadoDto nuevoArticulomanufacturadoDto) {
        Categoria categoria = repoCategoria.findById(nuevoArticulomanufacturadoDto.getIdCategoria()).get();

        ArticuloManufacturado articuloManufacturado = manufacturadoMapper.nuevoArticuloManufacturadoDtoToArticuloManufacturado(nuevoArticulomanufacturadoDto);
        articuloManufacturado.setCategoria(categoria);
        articuloManufacturado.setFechaBaja(nuevoArticulomanufacturadoDto.isDadoDeAlta() ? null : LocalDate.now());

        List<ArticuloManufacturadoDetalle> detalles = new ArrayList<>();
        for(ArticuloManufacturadoDetalleDto detalle : nuevoArticulomanufacturadoDto.getDetalles()){
            ArticuloInsumo articuloInsumo = repoArticuloInsumo.findById(detalle.getIdArticuloInsumo()).get();

            ArticuloManufacturadoDetalle articuloManufacturadoDetalle = ArticuloManufacturadoDetalle.builder()
                    .articuloManufacturado(articuloManufacturado)
                    .articuloInsumo(articuloInsumo)
                    .cantidad(detalle.getCantidad())
                    .build();
            detalles.add(articuloManufacturadoDetalle);
        }
        articuloManufacturado.setDetalles(detalles);

        articuloManufacturado = repoArticuloManufacturado.save(articuloManufacturado);

        if(articuloManufacturado.getPrecioVenta() == 0){
            entityManager.createNativeQuery("CALL modificarPrecioVenta(:_idArticulo)")
                    .setParameter("_idArticulo", articuloManufacturado.getIdArticulo())
                    .executeUpdate();
        }
    }

    //Dar de alta o baja a un artículo
    public void darDeAltaBaja(AltaBajaDto altaBajaDto) {
        ArticuloManufacturado articuloManufacturado = repoArticuloManufacturado.findById(altaBajaDto.getId()).get();

        articuloManufacturado.setFechaBaja(altaBajaDto.isDadoDeAlta() ? null : LocalDate.now());
        repoArticuloManufacturado.save(articuloManufacturado);
    }

    //Devuelve las páginas con la información de los artículos manufacturados para el ABM
    public Page<InformacionArticuloManufacturadoDto> mostrarArticulosAbm(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArticuloManufacturado> paginaArticulosAbm = repoArticuloManufacturado.findAll(pageable);

        return paginaArticulosAbm.map(articulo -> {
            InformacionArticuloManufacturadoDto dto = manufacturadoMapper.articuloManufacturadoToInformacionArticuloManufacturadoDto(articulo);
            dto.setDadoDeAlta(articulo.getFechaBaja() != null ? false : true);
            return dto;
        });
    }

    //Modifica un artículo manufacturado
    @Transactional
    public void actualizarArticulo(Long id, InformacionArticuloManufacturadoDto dto) {
        ArticuloManufacturado articulo = repoArticuloManufacturado.findById(id).get();

        articulo.setNombre(dto.getNombre());
        articulo.setDescripcion(dto.getDescripcion());
        articulo.setReceta(dto.getReceta());
        articulo.setTiempoDeCocina(dto.getTiempoDeCocina());

        Categoria categoria = repoCategoria.findById(dto.getIdCategoria()).get();
        articulo.setCategoria(categoria);

        if (dto.getImagenDto() != null) {
            ImagenArticulo imagen = imagenMapper.imagenDtoToImagenArticulo(dto.getImagenDto());
            articulo.setImagen(imagen);
        }

        articulo.getDetalles().clear();
        for (InformacionDetalleDto detalleDto : dto.getDetalles()) {
            ArticuloInsumo insumo = repoArticuloInsumo.findById(detalleDto.getIdArticuloInsumo()).get();

            ArticuloManufacturadoDetalle detalle = new ArticuloManufacturadoDetalle();
            detalle.setArticuloInsumo(insumo);
            detalle.setCantidad(detalleDto.getCantidad());
            detalle.setArticuloManufacturado(articulo);

            articulo.getDetalles().add(detalle);
        }

        articulo = repoArticuloManufacturado.save(articulo);

        if(!dto.isPrecioModificado()){
            entityManager.createNativeQuery("CALL modificarPrecioVenta(:_idArticulo)")
                    .setParameter("_idArticulo", articulo.getIdArticulo())
                    .executeUpdate();
        }
    }
}
