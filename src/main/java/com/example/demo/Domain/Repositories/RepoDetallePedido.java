package com.example.demo.Domain.Repositories;

import com.example.demo.Domain.Entities.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoDetallePedido extends JpaRepository<DetallePedido, Long> {
}
