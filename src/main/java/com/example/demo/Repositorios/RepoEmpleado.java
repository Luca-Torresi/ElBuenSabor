package com.example.demo.Repositorios;

import com.example.demo.Entidades.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoEmpleado extends JpaRepository<Empleado, Long> {
}
