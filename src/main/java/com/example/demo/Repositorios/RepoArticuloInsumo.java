package com.example.demo.Repositorios;

import com.example.demo.Entidades.ArticuloInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoArticuloInsumo extends JpaRepository<ArticuloInsumo, Long> {
}
