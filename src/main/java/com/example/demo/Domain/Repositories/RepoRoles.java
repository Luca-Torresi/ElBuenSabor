package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoRoles extends JpaRepository<Roles, Long> {
    // Método para encontrar un rol por su nombre (ej. "CLIENTE", "ADMIN")
    Optional<Roles> findByName(String name);

    // Método para encontrar un rol por su ID de Auth0
    Optional<Roles> findByAuth0RoleId(String auth0RoleId);
}
