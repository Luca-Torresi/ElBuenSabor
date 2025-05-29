package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.ImagenCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoImagenCategoria extends JpaRepository<ImagenCategoria, Long> {
}
