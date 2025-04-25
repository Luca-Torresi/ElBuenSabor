package com.example.demo.Repositorios;

import com.example.demo.Entidades.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoDetallePedido extends JpaRepository<DetallePedido, Long> {
}
