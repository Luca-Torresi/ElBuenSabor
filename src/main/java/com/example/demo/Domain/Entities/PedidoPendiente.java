package com.example.demo.Domain.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@NamedStoredProcedureQuery(
        name = "evaluarStockParaPedido",
        procedureName = "evaluarStockParaPedido",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "_idPedidoPendiente", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "_faltantes", type = String.class)
        }
)
@Data @Builder @AllArgsConstructor @NoArgsConstructor
@Entity @Table
public class PedidoPendiente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedidoPendiente;

    @OneToMany(mappedBy = "pedidoPendiente", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DetallePedidoPendiente> detalles;
}


