package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoProvincia extends JpaRepository<Provincia, Long> {
    Optional<Provincia> findByNombreIgnoreCase(String nombre);
}
