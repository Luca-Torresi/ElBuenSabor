package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoDireccion extends JpaRepository<Direccion, Long> {
    @Query(value = "SELECT * FROM direccion WHERE idCliente = :idCliente AND activo = true", nativeQuery = true)
    List<Direccion> findByClienteIdAndActivoTrue(@Param("idCliente") Long idCliente);
}
