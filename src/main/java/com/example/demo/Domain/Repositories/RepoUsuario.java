package com.example.demo.Domain.Repositories;

import com.example.demo.Application.DTO.Usuario.UsuarioRespDto;
import com.example.demo.Domain.Entities.Articulo;
import com.example.demo.Domain.Entities.Imagen;
import com.example.demo.Domain.Entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoUsuario extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByIdAuth0(String idAuth0);

    @Query("SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.rol ")
    List<Usuario> findAllUser();

    Optional<Usuario> findByImagen(Imagen imagen);
}