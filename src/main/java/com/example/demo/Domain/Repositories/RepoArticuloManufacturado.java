package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.ArticuloManufacturado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoArticuloManufacturado extends JpaRepository<ArticuloManufacturado, Long> {
}
