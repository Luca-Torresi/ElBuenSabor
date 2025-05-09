package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.ActualizacionCosto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoActualizacionCosto extends JpaRepository<ActualizacionCosto, Long> {
}
