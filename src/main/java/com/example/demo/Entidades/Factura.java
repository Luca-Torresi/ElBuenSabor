package com.example.demo.Entidades;

import com.example.demo.Enumeraciones.MetodoDePago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Factura {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFactura;
    private LocalDateTime fechaYHora;
    private double total;

    @Enumerated(EnumType.STRING)
    private MetodoDePago metodoDePago;

    @OneToOne
    @JoinColumn(name = "idPedido")
    private Pedido pedido;
}
