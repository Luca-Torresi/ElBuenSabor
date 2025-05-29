package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.PedidoPendiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoPedidoPendiente extends JpaRepository<PedidoPendiente, Long> {
    @Procedure(procedureName = "evaluarStockParaPedido")
    String evaluarStockParaPedido(@Param("_idPedidoPendiente") Long _idPedidoPendiente);
}
