package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoDireccion extends JpaRepository<Direccion, Long> {
}
