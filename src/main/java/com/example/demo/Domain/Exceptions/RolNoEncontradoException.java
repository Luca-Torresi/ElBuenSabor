package com.example.demo.Domain.Exceptions;

public class RolNoEncontradoException extends RuntimeException {
    public RolNoEncontradoException(String roleId) {
        super("Rol de Auth0 no encontrado en BD local: " + roleId);
    }
}
