package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoCliente extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);

    boolean existsByIdAuth0(String idAuth0);

    Optional<Cliente> findByIdAuth0(String idAuth0);
}
