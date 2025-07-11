package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Categoria.CategoriaDto;
import com.example.demo.Application.DTO.Categoria.NuevaCategoriaDto;
import com.example.demo.Application.DTO.Categoria.NuevoMargenGananciaDto;
import com.example.demo.Application.DTO.Generic.AltaBajaDto;
import com.example.demo.Application.Mapper.CategoriaMapper;
import com.example.demo.Domain.Entities.Categoria;
import com.example.demo.Domain.Repositories.RepoCategoria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceCategoria {
    private final RepoCategoria repoCategoria;
    private final CategoriaMapper categoriaMapper;

    //Persiste la nueva categoría en la base de datos
    public Categoria cargarNuevaCategoria(NuevaCategoriaDto nuevaCategoriaDto) {
        Categoria categoria = categoriaMapper.nuevaCategoriaDtoToCategoria(nuevaCategoriaDto);

        if (nuevaCategoriaDto.getIdCategoriaPadre() != null) {
            Categoria categoriaPadre = repoCategoria.findById(nuevaCategoriaDto.getIdCategoriaPadre())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada"));
            categoria.setCategoriaPadre(categoriaPadre);
        } else {
            categoria.setCategoriaPadre(null);
        }

        categoria.setFechaBaja(nuevaCategoriaDto.isDadoDeAlta() ? null : LocalDate.now());

        return repoCategoria.save(categoria);
    }

    //Dar de alta o baja a una categoría
    public void darDeAltaBaja(Long idCategoria) {
        Categoria categoria = repoCategoria.findById(idCategoria).get();
        categoria.setFechaBaja(
                categoria.getFechaBaja() != null ? null : LocalDate.now()
        );
        repoCategoria.save(categoria);
    }

    //Devuelve una lista con todas las categorías
    public List<CategoriaDto> listarCategoriasCatalogo(){
        List<Categoria> categorias = repoCategoria.findAll();

        return categorias.stream()
                .map(categoriaMapper::categoriaToCategoriaDto)
                .collect(Collectors.toList());
    }

    //Devuelve los datos de una categoría única
    public String buscarCategoriaPorId(Long id) {
        Categoria categoria = repoCategoria.findById(id).get();
        return categoria.getNombre();
    }

    public Categoria actualizarCategoria(Long idCategoria, NuevaCategoriaDto dto) {
        Categoria categoria = repoCategoria.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoria.setNombre(dto.getNombre());
        categoria.setMargenGanancia(dto.getMargenGanancia());
        categoria.setFechaBaja(dto.isDadoDeAlta() ? null : LocalDate.now());

        if (dto.getIdCategoriaPadre() != null) {
            Categoria padre = repoCategoria.findById(dto.getIdCategoriaPadre())
                    .orElseThrow(() -> new RuntimeException("Categoría padre no encontrada"));
            categoria.setCategoriaPadre(padre);
        } else {
            categoria.setCategoriaPadre(null);
        }

        categoria.setImagen(categoriaMapper.nuevaCategoriaDtoToCategoria(dto).getImagen());

        return repoCategoria.save(categoria);
    }

    //Obtiene de la base de datos únicamente los nombres de las categorías
    public List<String> nombresCategorias(){
        List<String> nombres = new ArrayList<>();

        List<Categoria> categorias = repoCategoria.findAll();
        for (Categoria categoria : categorias) {
            nombres.add(categoria.getNombre());
        }
        return nombres;
    }

     /*
    //Actualiza el margen de ganancia de la categoría correspondiente pasada como parámetro
    public void modificarMargenGanancia(NuevoMargenGananciaDto nuevoMargenGananciaDto) {
        Categoria categoria = repoCategoria.findById(nuevoMargenGananciaDto.getIdCategoria()).get();

        categoria.setMargenGanancia(nuevoMargenGananciaDto.getMargenGanancia());
        repoCategoria.save(categoria);
    }
    */
}
