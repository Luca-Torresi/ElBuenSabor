package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoEmpleado extends JpaRepository<Empleado, Long> {
    @Query(value = "SELECT * FROM empleado WHERE email = :email", nativeQuery = true)
    Empleado findByEmail(@Param("email") String email);
}
