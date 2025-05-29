package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoProvincia extends JpaRepository<Provincia, Long> {
}
