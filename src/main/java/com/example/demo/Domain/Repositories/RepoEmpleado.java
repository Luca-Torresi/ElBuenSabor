package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoEmpleado extends JpaRepository<Empleado, Long> {
}