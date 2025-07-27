package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.ArticuloNoElaborado.*;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceArticuloNoElaborado {
    private final RepoArticuloNoElaborado repoArticuloNoElaborado;
    private final NoElaboradoMapper noElaboradoMapper;
    private final RepoCategoria repoCategoria;
    private final EntityManager entityManager;

    //Persiste en la base de datos un nuevo artículo manufacturado
    @Transactional
    public ArticuloNoElaborado nuevoArticulo(NuevoArticuloNoElaboradoDto nuevoArticuloDto) { // <--- Ahora devuelve la entidad
        Categoria categoria = repoCategoria.findById(nuevoArticuloDto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada para el artículo no elaborado"));

        ArticuloNoElaborado articulo = noElaboradoMapper.nuevoArticuloNoElaboradoDtoToArticuloNoElaborado(nuevoArticuloDto);
        articulo.setCategoria(categoria);
        articulo.setFechaBaja(nuevoArticuloDto.isDadoDeAlta() ? null : LocalDate.now());

        articulo = repoArticuloNoElaborado.save(articulo);

        if(articulo.getPrecioVenta() == 0){
            entityManager.createNativeQuery("CALL modificarPrecioVenta(:_idArticulo)")
                    .setParameter("_idArticulo", articulo.getIdArticulo())
                    .executeUpdate();
        }
        return articulo;
    }

    // Modifica un artículo no elaborado existente
    @Transactional
    public void actualizarArticulo(Long id, InformacionArticuloNoElaboradoDto dto) {
        ArticuloNoElaborado articulo = repoArticuloNoElaborado.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no elaborado no encontrado: " + id));

        articulo.setNombre(dto.getNombre());
        articulo.setDescripcion(dto.getDescripcion());
        articulo.setPrecioVenta(dto.getPrecioVenta());

        Categoria categoria = repoCategoria.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada para el artículo no elaborado: " + dto.getIdCategoria()));
        articulo.setCategoria(categoria);

        articulo = repoArticuloNoElaborado.save(articulo);

        if(!dto.isPrecioModificado()){
            entityManager.createNativeQuery("CALL modificarPrecioVenta(:_idArticulo)")
                    .setParameter("_idArticulo", articulo.getIdArticulo())
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

    public void recargaStock(ArregloRecargaNoElaboradoDto arregloDto){
        for (RecargaNoElaboradoDto dto: arregloDto.getRecargas()){
            ArticuloNoElaborado articulo = repoArticuloNoElaborado.findById(dto.getIdArticulo()).get();

            articulo.setStock(articulo.getStock() + dto.getCantidad());
            repoArticuloNoElaborado.save(articulo);
        }
    }

    public List<NoElaboradoNombreDto> listaArticulosNoElaborados() {
        List<ArticuloNoElaborado> articulos = repoArticuloNoElaborado.findAll();

        List<NoElaboradoNombreDto> lista = new ArrayList<>();
        for (ArticuloNoElaborado articulo: articulos){
            NoElaboradoNombreDto dto = noElaboradoMapper.noElaboradoToNoElaboradoNombreDto(articulo);
            lista.add(dto);
        }
        return lista;
    }
}
