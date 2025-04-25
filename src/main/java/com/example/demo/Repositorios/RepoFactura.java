package com.example.demo.Repositorios;

import com.example.demo.Entidades.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoFactura extends JpaRepository<Factura,Long> {
}
