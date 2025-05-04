package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Usuario.PerfilEmpleadoDto;
import com.example.demo.Domain.Service.ServiceEmpleado;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/empleado")
public class ControllerEmpleado {

    private final ServiceEmpleado serviceEmpleado;

    public ControllerEmpleado(ServiceEmpleado serviceEmpleado) {
        this.serviceEmpleado = serviceEmpleado;
    }

    @GetMapping("/datos")
    public ResponseEntity<PerfilEmpleadoDto> datos(@AuthenticationPrincipal OidcUser _empleado) {
        return ResponseEntity.ok(
                serviceEmpleado.datos(_empleado)
        );
    }
}
