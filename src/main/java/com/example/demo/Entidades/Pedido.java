package com.example.demo.Entidades;

import com.example.demo.Enumeraciones.EstadoPedido;
import com.example.demo.Enumeraciones.TipoEnvio;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class Pedido {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;
    private LocalDateTime fechaYHora;

    @OneToMany(mappedBy = "pedido")
    @JsonManagedReference
    private List<DetallePedido> detalles;

    @Enumerated(EnumType.STRING)
    private TipoEnvio tipoEnvio;
    @Enumerated(EnumType.STRING)
    private EstadoPedido estadoPedido;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    @JsonBackReference
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
