package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoFactura extends JpaRepository<Factura,Long> {
}
