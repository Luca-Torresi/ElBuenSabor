package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.ImagenInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoImagenInsumo extends JpaRepository<ImagenInsumo, Long> {
}
