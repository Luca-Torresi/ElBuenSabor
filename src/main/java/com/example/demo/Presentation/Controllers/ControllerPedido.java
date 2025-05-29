package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Pedido.CambioDeEstadoDto;
import com.example.demo.Application.DTO.Pedido.HistorialDePedidosDto;
import com.example.demo.Application.DTO.Pedido.PedidoDto;
import com.example.demo.Domain.Service.ServicePedido;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedido")
public class ControllerPedido {
    private final ServicePedido servicePedido;

    //Recibe la información correspondiente a un nuevo pedido
    @PostMapping("/nuevo")
    public String nuevoPedido(@AuthenticationPrincipal OidcUser _cliente, @RequestBody PedidoDto pedidoDto) {
        return servicePedido.generarNuevoPedido(_cliente, pedidoDto);
    }

    //El cliente cancela el pedido
    @PatchMapping("/cancelado/{idPedido}")
    public void cancelarPedido(@PathVariable Long idPedido) {
        servicePedido.cancelarPedido(idPedido);
    }

    //El cajero cambia el estado del pedido, acepta o rechaza el pedido
    @PatchMapping("/cambioDeEstado")
    public void cambioDeEstado(@RequestBody CambioDeEstadoDto cambioDeEstadoDto) {
        servicePedido.cambiarEstado(cambioDeEstadoDto);
    }

    //El cocinero marca el pedido como 'listo'
    @PatchMapping("/listo/{idPedido}")
    public void pedidoListoParaEntrega(@PathVariable Long idPedido){
        servicePedido.pedidoListo(idPedido);
    }

    //Devuelve el historial de pedidos realizados por un cliente
    @GetMapping("/pedidosRealizados/{idCliente}")
    public ResponseEntity<HistorialDePedidosDto> pedidosRealizados(@PathVariable Long idCliente){
        HistorialDePedidosDto historialDePedidosDto = servicePedido.mostrarHistorialDePedidos(idCliente);

        return ResponseEntity.ok(historialDePedidosDto);
    }
}
