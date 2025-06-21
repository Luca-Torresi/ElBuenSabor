package com.example.demo.Domain.Exceptions;

public class DireccionInvalidaException extends RuntimeException {
    public DireccionInvalidaException(String detalle) {
        super("Dirección inválida: " + detalle);
    }
}
