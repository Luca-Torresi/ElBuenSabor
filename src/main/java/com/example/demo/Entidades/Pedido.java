package com.example.demo.Entidades;

import com.example.demo.Enumeraciones.EstadoPedido;
import com.example.demo.Enumeraciones.TipoEnvio;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Pedido {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPedido;
    private LocalDateTime fechaYHora;

    @Enumerated(EnumType.STRING)
    private TipoEnvio tipoEnvio;
    @Enumerated(EnumType.STRING)
    private EstadoPedido estadoPedido;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "idAdministrador")
    private Empleado administrador;
    @ManyToOne
    @JoinColumn(name = "idCajero")
    private Empleado cajero;
    @ManyToOne
    @JoinColumn(name = "idCocinero")
    private Empleado cocinero;
    @ManyToOne
    @JoinColumn(name = "idRepartidor")
    private Empleado repartidor;
}
