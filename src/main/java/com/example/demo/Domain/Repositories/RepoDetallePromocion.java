package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.DetallePromocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RepoDetallePromocion extends JpaRepository<DetallePromocion, Long> {

    List<DetallePromocion> findByPromocionId(Long idPromocion);
}
