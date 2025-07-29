package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.ArticuloInsumo;
import com.example.demo.Domain.Entities.Pedido;
import com.example.demo.Domain.Entities.RubroInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoRubroInsumo extends JpaRepository<RubroInsumo, Long> {
}
