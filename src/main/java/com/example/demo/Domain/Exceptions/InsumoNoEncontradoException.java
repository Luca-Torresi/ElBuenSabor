package com.example.demo.Domain.Exceptions;

public class InsumoNoEncontradoException extends RuntimeException {
    public InsumoNoEncontradoException(String message) {
        super(message);
    }
}
