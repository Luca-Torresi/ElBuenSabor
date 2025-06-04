package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.ImagenArticulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoImagenArticulo extends JpaRepository<ImagenArticulo, Long> {
}
