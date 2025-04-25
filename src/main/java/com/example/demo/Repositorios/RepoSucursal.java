package com.example.demo.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoSucursal extends JpaRepository<RepoSucursal, Long> {
}
