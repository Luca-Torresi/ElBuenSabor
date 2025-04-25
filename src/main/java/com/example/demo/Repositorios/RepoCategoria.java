package com.example.demo.Repositorios;

import com.example.demo.Entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoCategoria extends JpaRepository<Categoria, Long> {
}
