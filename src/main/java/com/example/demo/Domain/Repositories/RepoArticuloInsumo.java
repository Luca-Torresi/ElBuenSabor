package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.ArticuloInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoArticuloInsumo extends JpaRepository<ArticuloInsumo, Long> {
}
