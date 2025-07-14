package com.example.demo.Presentation.Handlers;

import com.auth0.exception.Auth0Exception;
import com.example.demo.Domain.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Antes de persistir un nuevo pedido en la base de datos, se verifica si hay stock suficiente para su elaboración
    //En caso de que esto no ocurra, se lanza la siguiente excepción
    @ExceptionHandler(InsumosInsuficientesException.class)
    public ResponseEntity<?> manejarStockInsuficiente(InsumosInsuficientesException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    //Si no se encuentra el pedido se lanza el siguiente error
    @ExceptionHandler(PedidoNoEncontradoException.class)
    public ResponseEntity<?> manejarPedidoNoEncontrado(PedidoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    //Si surge un problema cuando se llama al procedimiento almacenado de la base de datos para actualizar los precios de todos los artículos, se lanza la siguiente excepción
    @ExceptionHandler(ActualizacionPreciosException.class)
    public ResponseEntity<String> handleActualizacionPreciosException(ActualizacionPreciosException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    //Se lanza la siguiente excepción si no se encuentra al cliente en la base de datos
    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<String> handleClienteNoEncontradoException(ClienteNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(EmpleadoNotFoundException.class)
    public ResponseEntity<String> handleEmpleadoNotFound(EmpleadoNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DireccionInvalidaException.class)
    public ResponseEntity<String> handleDireccionInvalida(DireccionInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(RolNoEncontradoException.class)
    public ResponseEntity<String> handleRolNoEncontrado(RolNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Auth0Exception.class)
    public ResponseEntity<String> handleAuth0Exception(Auth0Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ex.getMessage());
    }

    // Errores de negocio genéricos
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Errores por argumentos inválidos
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // Errores inesperados (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error inesperado: " + ex.getMessage());
    }
}