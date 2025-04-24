package com.example.demo.Entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class DetallePedido {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDetallePedido;
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "idPedido")
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "idArticulo")
    private Articulo articulo;
}
