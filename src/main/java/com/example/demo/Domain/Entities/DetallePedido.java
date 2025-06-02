package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class DetallePedido {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetallePedido;

    @ManyToOne @JoinColumn(name = "idArticulo")
    private Articulo articulo;
    private int cantidad;

    @ManyToOne @JoinColumn(name = "idPedido")
    @JsonBackReference
    private Pedido pedido;
}
