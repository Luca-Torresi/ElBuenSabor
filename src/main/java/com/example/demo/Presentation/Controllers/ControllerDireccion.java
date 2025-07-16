package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Direccion.ArregloDireccionDto;
import com.example.demo.Domain.Service.ServiceDireccion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    //Editar dirección

    //Eliminar dirección
}
