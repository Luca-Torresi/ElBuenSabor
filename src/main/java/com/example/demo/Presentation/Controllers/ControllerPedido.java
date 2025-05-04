package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Pedido.PedidoDto;
import com.example.demo.Domain.Service.ServicePedido;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedido")
public class ControllerPedido {

    private final ServicePedido servicePedido;

    public ControllerPedido(ServicePedido servicePedido) {
        this.servicePedido = servicePedido;
    }

    @PostMapping("/nuevo")
    public void nuevoPedido(@AuthenticationPrincipal OidcUser _cliente, @RequestBody PedidoDto pedidoDto) {
        servicePedido.generarNuevoPedido(_cliente, pedidoDto);
    }
}
