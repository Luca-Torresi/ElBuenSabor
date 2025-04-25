package com.example.demo.Repositorios;

import com.example.demo.Entidades.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoPromocion extends JpaRepository<Promocion, Long> {
}
