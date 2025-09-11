package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Articulo.ArticuloDto;
import com.example.demo.Application.DTO.Articulo.ArticuloNombreDto;
import com.example.demo.Application.DTO.Articulo.ArticuloPromocionDto;
import com.example.demo.Application.Mapper.ArticuloMapper;
import com.example.demo.Domain.Entities.Articulo;
import com.example.demo.Domain.Entities.ArticuloNoElaborado;
import com.example.demo.Domain.Exceptions.ArticuloNoEncontradoException;
import com.example.demo.Domain.Repositories.RepoArticulo;
import com.example.demo.Domain.Repositories.RepoArticuloNoElaborado;
import com.example.demo.Domain.Exceptions.ActualizacionPreciosException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

            if (articulo.getEsManufacturado()) {
                dto.setPuedeElaborarse(repoArticulo.sePuedeElaborar(articulo.getIdArticulo()));
            } else {
                Optional<ArticuloNoElaborado> optional = repoArticuloNoElaborado.findById(articulo.getIdArticulo());
                boolean puedeElaborarse = optional.map(noElaborado -> noElaborado.getStock() >= 1).orElse(false);
                dto.setPuedeElaborarse(puedeElaborarse);
            }

            return dto;
        });
    }

    public Articulo obtenerArticuloPorId(Long idArticulo) {
        return repoArticulo.findById(idArticulo)
                .orElseThrow(() -> new ArticuloNoEncontradoException("No se encontró el artículo con ID: " + idArticulo));
    }

    public ArticuloDto obtenerInformacionArticulo(Long idArticulo) {
        Articulo articulo = repoArticulo.findById(idArticulo)
                .orElseThrow(() -> new ArticuloNoEncontradoException("No se encontró el artículo con ID: " + idArticulo));

        return articuloMapper.articuloToArticuloDto(articulo);
    }

    //Actualiza los precios de todos los artículos manufacturados
    @Transactional
    public void actualizarPrecios(){
        try{
            entityManager.createNativeQuery("CALL actualizarPreciosArticulos()")
                    .executeUpdate();
        } catch (Exception e){
            throw new ActualizacionPreciosException("Error al actualizar los precios de los artículos");
        }
    }

    //Dar de alta o baja a un artículo
    @Transactional
    public void darDeAltaBaja(Long idArticulo) {
        Articulo articulo = repoArticulo.findById(idArticulo)
                .orElseThrow(() -> new ArticuloNoEncontradoException("No se encontró el artículo con ID: " + idArticulo));

        articulo.setFechaBaja(
                articulo.getFechaBaja() != null ? null : LocalDate.now()
        );
        repoArticulo.save(articulo);
    }
    //Para ver los articulos en las promociones
    public List<ArticuloPromocionDto> listarArticulosParaPromociones() {
        List<Articulo> articulos = repoArticulo.findAll();

        return articulos.stream().map(articulo -> {
            boolean activo = articulo.getFechaBaja() == null;
            boolean puedeElaborarse = false;

            try {
                puedeElaborarse = puedeElaborarse(articulo.getIdArticulo());
            } catch (Exception e) {
                // Log del error y continuar con false
                System.err.println("Error al verificar si puede elaborarse el artículo " +
                        articulo.getIdArticulo() + ": " + e.getMessage());
                puedeElaborarse = false;
            }

            return ArticuloPromocionDto.builder()
                    .idArticulo(articulo.getIdArticulo())
                    .nombre(articulo.getNombre())
                    .precioVenta(articulo.getPrecioVenta())
                    .activo(activo)
                    .puedeElaborarse(puedeElaborarse)
                    .disponible(activo && puedeElaborarse)
                    .build();
        }).collect(Collectors.toList());
    }

    //Obtiene de la base de datos una lista con los nombres de todos los artículos para ser mostrados dentro de un 'select'
    public List<ArticuloNombreDto> listaNombresArticulos(){
        List<Articulo> articulos = repoArticulo.findByFechaBajaIsNull();

        List<ArticuloNombreDto> lista = new ArrayList<ArticuloNombreDto>();
        for(Articulo articulo : articulos){
            ArticuloNombreDto dto = articuloMapper.articuloToArticuloNombreDto(articulo);
            lista.add(dto);
        }
        return lista;
    }



    public boolean puedeElaborarse(Long idArticulo) {
        Articulo articulo = obtenerArticuloPorId(idArticulo);

        // Si está dado de baja, no se puede elaborar
        if (articulo.getFechaBaja() != null) {
            return false;
        }

        // Verificar si esManufacturado es null y manejarlo
        Boolean esManufacturado = articulo.getEsManufacturado();

        // Si es null, asumir que no es manufacturado (o manejar según tu lógica de negocio)
        if (esManufacturado == null) {
            esManufacturado = false; // o true, según tu lógica
        }

        // Si es manufacturado, verificar insumos
        if (esManufacturado) {
            return repoArticulo.sePuedeElaborar(idArticulo);
        }
        // Si no es manufacturado, verificar stock directo
        else {
            Optional<ArticuloNoElaborado> noElaborado = repoArticuloNoElaborado.findById(idArticulo);
            return noElaborado.map(art -> art.getStock() >= 1).orElse(false);
        }
    }


}
