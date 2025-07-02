package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoRol extends JpaRepository<Rol, Long> {
    // Método para encontrar un rol por su nombre (ej. "CLIENTE", "ADMIN")
    Optional<Rol> findByNombre(String nombre);

    // Método para encontrar un rol por su ID de Auth0
    Optional<Rol> findByAuth0RoleId(String auth0RoleId);
}
