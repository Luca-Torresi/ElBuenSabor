package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Articulo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoArticulo extends JpaRepository<Articulo, Long> {
}
