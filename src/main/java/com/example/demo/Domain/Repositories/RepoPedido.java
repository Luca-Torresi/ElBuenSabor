package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepoPedido extends JpaRepository<Pedido, Long> {
    @Query(value = "SELECT * FROM pedido WHERE idCliente = :idCliente ORDER BY fechayhora DESC", nativeQuery = true)
    List<Pedido> findPedidosByIdCliente(@Param("idCliente") Long idCliente);
}
