package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.ArticuloNoElaborado.InformacionArticuloNoElaboradoDto;
import com.example.demo.Application.Mapper.NoElaboradoMapper;
import com.example.demo.Domain.Entities.ArticuloNoElaborado;
import com.example.demo.Domain.Repositories.RepoArticuloNoElaborado;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceArticuloNoElaborado {
    private final RepoArticuloNoElaborado repoArticuloNoElaborado;
    private final NoElaboradoMapper noElaboradoMapper;

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
