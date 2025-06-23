package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Pedido.HistorialDePedidosDto;
import com.example.demo.Application.DTO.Pedido.NuevoPedidoDto;
import com.example.demo.Application.DTO.Pedido.PedidoDto;
import com.example.demo.Domain.Service.ServicePedido;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public String nuevoPedido(@AuthenticationPrincipal OidcUser _cliente, @RequestBody NuevoPedidoDto nuevoPedidoDto) {
        return servicePedido.generarNuevoPedido(_cliente, nuevoPedidoDto);
    }

    //Devuelve una lista con todos los pedidos para ser gestionados por el cajero
    @GetMapping("/lista")
    public Page<PedidoDto> mostrarPedidos(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "9") int size){
        return servicePedido.listarTodosLosPedidos(page,size);
    }

    //El cliente cancela el pedido
    @PutMapping("/cancelado/{idPedido}")
    public ResponseEntity cancelarPedido(@PathVariable Long idPedido) {
        servicePedido.cancelarPedido(idPedido);
        return ResponseEntity.ok().build();
    }

    //El cajero confirma el pedido y pasa al estado 'EN_PREPARACION'
    @PutMapping("/confirmado/{idPedido}")
    public ResponseEntity confirmarPedido(@PathVariable Long idPedido) {
        servicePedido.confirmarPedido(idPedido);
        return ResponseEntity.ok().build();
    }

    //El cajero rechaza el pedido
    @PutMapping("/rechazado/{idPedido}")
    public ResponseEntity rechazarPedido(@PathVariable Long idPedido) {
        servicePedido.rechazarPedido(idPedido);
        return ResponseEntity.ok().build();
    }

    //El cocinero marca el pedido como 'listo'
    @PutMapping("/listo/{idPedido}")
    public ResponseEntity pedidoListoParaEntrega(@PathVariable Long idPedido){
        servicePedido.pedidoListo(idPedido);
        return ResponseEntity.ok().build();
    }

    //Devuelve el historial de pedidos realizados por un cliente
    @GetMapping("/pedidosRealizados/{idCliente}")
    public ResponseEntity<HistorialDePedidosDto> pedidosRealizados(@PathVariable Long idCliente){
        HistorialDePedidosDto historialDePedidosDto = servicePedido.mostrarHistorialDePedidos(idCliente);

        return ResponseEntity.ok(historialDePedidosDto);
    }
}
