package com.example.demo.Repositorios;

import com.example.demo.Entidades.ArticuloManufacturado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoArticuloManufacturado extends JpaRepository<ArticuloManufacturado, Long> {
}
