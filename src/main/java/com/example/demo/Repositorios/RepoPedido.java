package com.example.demo.Repositorios;

import com.example.demo.Entidades.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoPedido extends JpaRepository<Pedido, Long> {
}
