package com.example.demo.Repositorios;

import com.example.demo.Entidades.ImagenManufacturado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoImagenManufacturado extends JpaRepository<ImagenManufacturado, Long> {
}
