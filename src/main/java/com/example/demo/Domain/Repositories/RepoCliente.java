package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoCliente extends JpaRepository<Cliente, Long> {
    @Query(value = "SELECT * FROM cliente WHERE email = :email", nativeQuery = true)
    Cliente findByEmail(@Param("email") String email);

    boolean existsByIdAuth0(String idAuth0);
}
