package com.example.demo.Domain.Exceptions;

public class ClienteNoRegistradoException extends RuntimeException {
    public ClienteNoRegistradoException(String mensaje) {
        super(mensaje);
    }
}
