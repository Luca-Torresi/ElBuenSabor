package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Factura;
import com.example.demo.Domain.Entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepoFactura extends JpaRepository<Factura,Long> {
    @Query(value = "SELECT * FROM factura WHERE idPedido = :idPedido", nativeQuery = true)
    Factura findByIdPedido(@Param("idPedido") Long idPedido);
}
