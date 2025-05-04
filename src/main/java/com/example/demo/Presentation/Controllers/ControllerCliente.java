package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Usuario.PerfilClienteDto;
import com.example.demo.Domain.Service.ServiceCliente;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ControllerCliente {

    private final ServiceCliente serviceCliente;

    public ControllerCliente(ServiceCliente serviceCliente) {
        this.serviceCliente = serviceCliente;
    }

    @GetMapping("/datos")
    public ResponseEntity<PerfilClienteDto> datos(@AuthenticationPrincipal OidcUser _cliente) {
        return ResponseEntity.ok(
            serviceCliente.datos(_cliente)
        );
    }
}

/*
@PreAuthorize("isAuthenticated()") → Indica qye se debe estar loggeado para acceder al endpoint correspondiente
                                     En caso de no estar loggeado, deberá redirigir a la página para iniciar sesión.

@PreAuthorize("permitAll()") → Indica que se puede acceder al endpoint correspondiente sin necesidad de estar loggeado

@AuthenticationPrincipal → Sirve para inyectar automáticamente los datos del usuario autenticado
*/
