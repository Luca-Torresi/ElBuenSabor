package com.example.demo.Entidades;

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
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "idPedido")
    @JsonBackReference
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "idArticuloManufacturado")
    private ArticuloManufacturado articuloManufacturado;
}
