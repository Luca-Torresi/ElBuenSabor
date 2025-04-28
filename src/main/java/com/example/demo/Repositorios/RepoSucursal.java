package com.example.demo.Repositorios;

import com.example.demo.Entidades.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoSucursal extends JpaRepository<Sucursal, Long> {
}
