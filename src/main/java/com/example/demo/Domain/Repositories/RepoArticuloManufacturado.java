package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.ArticuloManufacturado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoArticuloManufacturado extends JpaRepository<ArticuloManufacturado, Long> {
}
