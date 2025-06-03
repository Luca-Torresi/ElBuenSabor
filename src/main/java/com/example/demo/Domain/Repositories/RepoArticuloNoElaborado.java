package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.ArticuloNoElaborado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoArticuloNoElaborado extends JpaRepository<ArticuloNoElaborado, Long> {
}
