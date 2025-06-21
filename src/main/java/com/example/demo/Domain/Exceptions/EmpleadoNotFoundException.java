package com.example.demo.Domain.Exceptions;

public class EmpleadoNotFoundException extends RuntimeException {
    public EmpleadoNotFoundException(Long id) {
        super("Empleado no encontrado con ID: " + id);
    }
}

