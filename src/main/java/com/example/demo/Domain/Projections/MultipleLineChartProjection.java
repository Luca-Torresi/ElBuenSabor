package com.example.demo.Domain.Projections;

public interface MultipleLineChartProjection {
    String getMes();
    Integer getClientesSinPedido();
    Integer getClientesConUnPedido();
    Integer getClientesRecurrentes();
}
