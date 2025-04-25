package com.example.demo.Repositorios;

import com.example.demo.Entidades.ImagenInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoImagenInsumo extends JpaRepository<ImagenInsumo, Long> {
}
