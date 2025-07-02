package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoPromocion extends JpaRepository<Promocion, Long> {
}
