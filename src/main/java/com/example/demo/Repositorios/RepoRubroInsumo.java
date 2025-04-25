package com.example.demo.Repositorios;

import com.example.demo.Entidades.RubroInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoRubroInsumo extends JpaRepository<RubroInsumo, Long> {
}
