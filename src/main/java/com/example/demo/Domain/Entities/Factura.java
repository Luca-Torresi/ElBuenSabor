package com.example.demo.Domain.Entities;

import com.example.demo.Domain.Enums.MetodoDePago;
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
public class Factura {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;
    private String nroComprobante;
    private LocalDateTime fechaYHora;
    private double total;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DetalleFactura> detalles;

    @Enumerated(EnumType.STRING)
    private MetodoDePago metodoDePago;

    @OneToOne
    @JoinColumn(name = "idPedido")
    private Pedido pedido;
}
