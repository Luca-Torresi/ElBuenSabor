package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.UnidadDeMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoUnidadDeMedida extends JpaRepository<UnidadDeMedida, Long> {
}
