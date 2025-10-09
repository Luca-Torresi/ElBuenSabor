package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Promocion.*;
import com.example.demo.Application.Mapper.DetallePromocionMapper;
import com.example.demo.Application.Mapper.PromocionMapper;
import com.example.demo.Domain.Entities.Articulo;
import com.example.demo.Domain.Entities.ArticuloManufacturado;
import com.example.demo.Domain.Entities.DetallePromocion;
import com.example.demo.Domain.Entities.Promocion;
import com.example.demo.Domain.Exceptions.ArticuloDadoDeBajaException;
import com.example.demo.Domain.Exceptions.ArticuloNoEncontradoException;
import com.example.demo.Domain.Repositories.RepoArticulo;
import com.example.demo.Domain.Repositories.RepoArticuloManufacturado;
import com.example.demo.Domain.Repositories.RepoDetallePromocion;
import com.example.demo.Domain.Repositories.RepoPromocion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicePromocion {
    private final RepoPromocion repoPromocion;
    private final PromocionMapper promocionMapper;
    private final RepoDetallePromocion repoDetallePromocion;
    private final RepoArticulo repoArticulo;
    private final RepoArticuloManufacturado repoArticuloManufacturado;
    private final DetallePromocionMapper detallePromocionMapper;

    //Cargar nueva promoción
    public Long nuevaPromocion(NuevaPromocionDto nuevaPromocionDto) {
        Promocion promocion = promocionMapper.promocionDtoToPromocion(nuevaPromocionDto);
        List<DetallePromocion> detallesPromocion = new ArrayList<>();
        int tiempoMaximo = 0;

        for (DetallePromocionDto detalleDto : nuevaPromocionDto.getDetalles()) {
            Articulo articulo = repoArticulo.findById(detalleDto.getIdArticulo())
                    .orElseThrow(() -> new ArticuloNoEncontradoException("No se encontró el artículo con ID: " + detalleDto.getIdArticulo()));

            if(articulo.getEsManufacturado()) {
                Optional<ArticuloManufacturado> articuloManufacturadoOpt = repoArticuloManufacturado.findById(detalleDto.getIdArticulo());

                if (articuloManufacturadoOpt.isPresent()) {
                    ArticuloManufacturado am = articuloManufacturadoOpt.get();

                    if (am.getTiempoDeCocina() > tiempoMaximo) {
                        tiempoMaximo = am.getTiempoDeCocina();
                    }
                }
            }

            DetallePromocion detalle = new DetallePromocion();
            detalle.setCantidad(detalleDto.getCantidad());
            detalle.setArticulo(articulo);
            detalle.setPromocion(promocion);

            detallesPromocion.add(detalle);
        }

        promocion.setTiempoDeCocina(tiempoMaximo);
        promocion.setDetalles(detallesPromocion);
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

    public PromocionCarritoDto obtenerPromocionByID(Long idPromocion) {
        Promocion promocion = repoPromocion.findById(idPromocion)
                .orElseThrow(() -> new ArticuloNoEncontradoException("No se encontró la promocion con ID: " + idPromocion));


        List<DetallePromocionDto> detallesDto = promocion.getDetalles().stream()
                .map(detallePromocionMapper::detallePromocionToDetallePromocionDto)
                .toList();
        PromocionResumenDto promocionResumenDto = calcularResumenPromocion(promocion.getIdPromocion());
        PromocionCarritoDto promocionCarritoDto = new PromocionCarritoDto(
                promocion.getIdPromocion(),
                promocion.getTitulo(),
                promocion.getDescripcion(),
                promocion.getImagen().getUrl(),
                promocion.getHorarioInicio(),
                promocion.getHorarioFin(),
                detallesDto,
                promocionResumenDto.getPrecioBase(),
                promocionResumenDto.getPrecioPromocional(),
                promocionResumenDto.getAhorro()
        );

        return promocionCarritoDto;
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
        int tiempoMaximo = 0;

        if (promocion.getDetalles() != null) {
            promocion.getDetalles().clear();
        } else {
            promocion.setDetalles(new ArrayList<>());
        }

        for (DetallePromocionDto detalleDto : dto.getDetalles()) {
            Articulo articulo = repoArticulo.findById(detalleDto.getIdArticulo())
                    .orElseThrow(() -> new ArticuloNoEncontradoException("No se encontró el artículo con ID: " + detalleDto.getIdArticulo()));

            if(articulo.getEsManufacturado()) {
                Optional<ArticuloManufacturado> articuloManufacturadoOpt = repoArticuloManufacturado.findById(detalleDto.getIdArticulo());
                if (articuloManufacturadoOpt.isPresent()) {
                    ArticuloManufacturado am = articuloManufacturadoOpt.get();

                    if (am.getTiempoDeCocina() > tiempoMaximo) {
                        tiempoMaximo = am.getTiempoDeCocina();
                    }
                }
            }

            DetallePromocion detalle = new DetallePromocion();
            detalle.setCantidad(detalleDto.getCantidad());
            detalle.setArticulo(articulo);
            detalle.setPromocion(promocion);

            promocion.getDetalles().add(detalle);
        }
        promocion.setTiempoDeCocina(tiempoMaximo);
        return repoPromocion.save(promocion);
    }

    //Dar de alta o baja una promoción
    public void darDeAltaBajaPromocion(Long idPromocion){
        Promocion promocion = repoPromocion.findById(idPromocion).get();
        List<DetallePromocion> detalles = repoDetallePromocion.findByPromocionIdPromocion(idPromocion);

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

    // Devuelve el precio total sumado de los artículos
    public Double calcularPrecioSugerido(List<NuevoDetallePromocionDto> detalles) {
        return detalles.stream()
                .mapToDouble(d -> {
                    Articulo articulo = repoArticulo.findById(d.getIdArticulo())
                            .orElseThrow(() -> new RuntimeException(
                                    "Artículo con id " + d.getIdArticulo() + " no encontrado"));
                    return articulo.getPrecioVenta() * d.getCantidad();
                })
                .sum();
    }

    // Devuelve un resumen con el precio base y lo que se ahorra con el promocional
    public PromocionResumenDto calcularResumenPromocion(Long idPromocion) {
        Promocion promocion = repoPromocion.findById(idPromocion)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada"));

        double precioBase = promocion.getDetalles().stream()
                .mapToDouble(d -> d.getArticulo().getPrecioVenta() * d.getCantidad())
                .sum();

        double precioPromo = promocion.getPrecioPromocion();
        double ahorro = precioBase - precioPromo;

        return new PromocionResumenDto(precioBase, precioPromo, ahorro);
    }
}
