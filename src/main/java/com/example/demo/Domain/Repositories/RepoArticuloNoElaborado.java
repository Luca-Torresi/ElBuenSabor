package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.ArticuloNoElaborado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoArticuloNoElaborado extends JpaRepository<ArticuloNoElaborado, Long> {
}
