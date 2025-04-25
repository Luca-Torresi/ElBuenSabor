package com.example.demo.Repositorios;

import com.example.demo.Entidades.UnidadDeMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoUnidadDeMedida extends JpaRepository<UnidadDeMedida, Long> {
}
