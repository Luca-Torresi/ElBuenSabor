package com.example.demo.Repositorios;

import com.example.demo.Entidades.ArticuloManufacturadoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoArticuloManufacturadoDetalle extends JpaRepository<ArticuloManufacturadoDetalle, Long> {
}
