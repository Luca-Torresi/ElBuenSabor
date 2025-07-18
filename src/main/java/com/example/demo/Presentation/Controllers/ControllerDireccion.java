package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Direccion.ArregloDireccionDto;
import com.example.demo.Application.DTO.Direccion.NuevaDireccionDto;
import com.example.demo.Domain.Entities.Direccion;
import com.example.demo.Domain.Service.ServiceDireccion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/direccion")
public class ControllerDireccion {
    private final ServiceDireccion serviceDireccion;

    //Devuelve las direcciones de un cliente para elegir al momento de hacer un pedido
    @GetMapping("/obtenerDireccionesCliente")
    public ResponseEntity<ArregloDireccionDto> obtenerDireccionesCliente(@AuthenticationPrincipal OidcUser cliente) {
        ArregloDireccionDto arregloDireccionDto = serviceDireccion.obtenerDireccionesCliente(cliente);
        return ResponseEntity.ok(arregloDireccionDto);
    }

    //Nueva dirección
    @PostMapping("/nueva")
    public ResponseEntity<Direccion> nuevaDireccion(@RequestBody NuevaDireccionDto dto) {
        Direccion direccion = serviceDireccion.nuevaDireccion(dto);
        return ResponseEntity.ok(direccion);
    }

    //Editar dirección
    @PutMapping("/modificar/{idDireccion}")
    public ResponseEntity<Direccion> modificarDireccion(@PathVariable Long idDireccion, @RequestBody NuevaDireccionDto dto) {
        Direccion direccion = serviceDireccion.modificarDireccion(idDireccion, dto);
        return ResponseEntity.ok(direccion);
    }

    //Eliminar dirección
    @DeleteMapping("/eliminar/{idDireccion}")
    public ResponseEntity<Void> eliminarDireccion(@PathVariable Long idDireccion) {
        serviceDireccion.eliminarDireccion(idDireccion);
        return ResponseEntity.noContent().build();
    }
}
