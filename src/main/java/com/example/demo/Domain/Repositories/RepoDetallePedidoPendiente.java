package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.DetallePedidoPendiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoDetallePedidoPendiente extends JpaRepository<DetallePedidoPendiente, Long> {
}
