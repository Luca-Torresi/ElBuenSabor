package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoUsuario extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
