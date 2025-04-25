package com.example.demo.Repositorios;

import com.example.demo.Entidades.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoEmpresa extends JpaRepository<Empresa, Long> {
}
