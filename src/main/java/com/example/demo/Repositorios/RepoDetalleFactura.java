package com.example.demo.Repositorios;

import com.example.demo.Entidades.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoDetalleFactura extends JpaRepository<DetalleFactura,Long> {
}
