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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceCategoria {
    private final RepoCategoria repoCategoria;
    private final CategoriaMapper categoriaMapper;

    //Persiste la nueva categoría en la base de datos
    public Categoria cargarNuevaCategoria(NuevaCategoriaDto nuevaCategoriaDto) {
        Categoria categoriaPadre = repoCategoria.findById(nuevaCategoriaDto.getIdCategoriaPadre()).get();

        Categoria categoria = categoriaMapper.nuevaCategoriaDtoToCategoria(nuevaCategoriaDto);
        categoria.setCategoriaPadre(categoriaPadre);
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

     /*
    //Actualiza el margen de ganancia de la categoría correspondiente pasada como parámetro
    public void modificarMargenGanancia(NuevoMargenGananciaDto nuevoMargenGananciaDto) {
        Categoria categoria = repoCategoria.findById(nuevoMargenGananciaDto.getIdCategoria()).get();

        categoria.setMargenGanancia(nuevoMargenGananciaDto.getMargenGanancia());
        repoCategoria.save(categoria);
    }
    */
}
