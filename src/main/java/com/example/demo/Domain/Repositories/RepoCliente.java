package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoCliente extends JpaRepository<Cliente, Long> {
}
