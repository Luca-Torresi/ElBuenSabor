package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoDepartamento extends JpaRepository<Departamento, Long> {

}
