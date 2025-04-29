package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoCategoria extends JpaRepository<Categoria, Long> {
}
