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
    private final EntityManager entityManager;

    //Persiste en la base de datos un nuevo artículo manufacturado
    @Transactional
    public ArticuloManufacturado nuevoArticulo(NuevoArticuloManufacturadoDto nuevoArticulomanufacturadoDto) {
        Categoria categoria = repoCategoria.findById(nuevoArticulomanufacturadoDto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        ArticuloManufacturado articuloManufacturado = manufacturadoMapper.nuevoArticuloManufacturadoDtoToArticuloManufacturado(nuevoArticulomanufacturadoDto);
        articuloManufacturado.setCategoria(categoria);
        articuloManufacturado.setFechaBaja(nuevoArticulomanufacturadoDto.isDadoDeAlta() ? null : LocalDate.now());

        List<ArticuloManufacturadoDetalle> detalles = new ArrayList<>();
        for(ArticuloManufacturadoDetalleDto detalle : nuevoArticulomanufacturadoDto.getDetalles()){
            ArticuloInsumo articuloInsumo = repoArticuloInsumo.findById(detalle.getIdArticuloInsumo())
                    .orElseThrow(() -> new RuntimeException("Insumo no encontrado: " + detalle.getIdArticuloInsumo()));

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

        return articuloManufacturado;
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
        ArticuloManufacturado articulo = repoArticuloManufacturado.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo manufacturado no encontrado: " + id));

        articulo.setNombre(dto.getNombre());
        articulo.setDescripcion(dto.getDescripcion());
        articulo.setReceta(dto.getReceta());
        articulo.setTiempoDeCocina(dto.getTiempoDeCocina());

        Categoria categoria = repoCategoria.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + dto.getIdCategoria()));
        articulo.setCategoria(categoria);

        articulo.getDetalles().clear();
        for (InformacionDetalleDto detalleDto : dto.getDetalles()) {
            ArticuloInsumo insumo = repoArticuloInsumo.findById(detalleDto.getIdArticuloInsumo())
                    .orElseThrow(() -> new RuntimeException("Insumo no encontrado: " + detalleDto.getIdArticuloInsumo()));

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
