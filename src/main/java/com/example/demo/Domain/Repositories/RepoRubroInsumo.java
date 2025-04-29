package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.RubroInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoRubroInsumo extends JpaRepository<RubroInsumo, Long> {
}
