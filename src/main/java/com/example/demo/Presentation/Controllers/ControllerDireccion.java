package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Direccion.ArregloDireccionDto;
import com.example.demo.Application.DTO.Direccion.NuevaDireccionDto;
import com.example.demo.Domain.Entities.Direccion;
import com.example.demo.Domain.Service.ServiceDireccion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/direccion")
public class ControllerDireccion {
    private final ServiceDireccion serviceDireccion;

    private String getAuth0IdFromAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getSubject();
        }
        throw new IllegalStateException("Usuario no autenticado o ID de Auth0 no disponible.");
    }

    //Devuelve las direcciones de un cliente para elegir al momento de hacer un pedido
    @GetMapping("/obtenerDireccionesCliente")
    public ResponseEntity<ArregloDireccionDto> obtenerDireccionesCliente() {

        String auth0Id = getAuth0IdFromAuthenticatedUser();

        ArregloDireccionDto arregloDireccionDto = serviceDireccion.obtenerDireccionesCliente(auth0Id);
        return ResponseEntity.ok(arregloDireccionDto);
    }

    //Nueva dirección
    @PostMapping("/nueva")
    public ResponseEntity<Direccion> nuevaDireccion(@RequestBody NuevaDireccionDto dto) {
        String auth0Id = getAuth0IdFromAuthenticatedUser();

        Direccion direccion = serviceDireccion.nuevaDireccion(auth0Id, dto);
        return ResponseEntity.ok(direccion);
    }

    //Editar dirección
    @PutMapping("/modificar/{idDireccion}")
    public ResponseEntity<Direccion> modificarDireccion(@PathVariable Long idDireccion, @RequestBody NuevaDireccionDto dto) {
        String auth0Id = getAuth0IdFromAuthenticatedUser();

        Direccion direccion = serviceDireccion.modificarDireccion(auth0Id, idDireccion, dto);
        return ResponseEntity.ok(direccion);
    }

    //Eliminar dirección
    @DeleteMapping("/eliminar/{idDireccion}")
    public ResponseEntity<Void> eliminarDireccion(@PathVariable Long idDireccion) {
        serviceDireccion.eliminarDireccion(idDireccion);
        return ResponseEntity.noContent().build();
    }
}
