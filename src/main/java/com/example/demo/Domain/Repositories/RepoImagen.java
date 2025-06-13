package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RepoImagen extends JpaRepository<Imagen, Long> {
    void deleteById(UUID idBd);
}
