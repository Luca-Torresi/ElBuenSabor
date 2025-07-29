package com.example.demo.Domain.Exceptions;

public class ArticuloNoEncontradoException extends RuntimeException {
    public ArticuloNoEncontradoException(String message) {
        super(message);
    }
}
