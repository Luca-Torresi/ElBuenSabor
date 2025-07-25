package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Imagen;
import com.example.demo.Domain.Entities.Promocion;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoPromocion extends JpaRepository<Promocion, Long> {
    List<Promocion> findByActivoTrue();
}
