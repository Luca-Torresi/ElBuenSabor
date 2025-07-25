package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Pedido.*;
import com.example.demo.Domain.Entities.Pedido;
import com.example.demo.Domain.Service.ServiceFactura;
import com.example.demo.Domain.Service.ServicePedido;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pedido")
public class ControllerPedido {
    private final ServicePedido servicePedido;
    private final ServiceFactura serviceFactura;

    //Recibe la información correspondiente a un nuevo pedido
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'CLIENTE')")
    @PostMapping("/nuevo")
    public ResponseEntity<Pedido> nuevoPedido(@AuthenticationPrincipal OidcUser _cliente, @RequestBody NuevoPedidoDto nuevoPedidoDto) {
        Pedido pedido = servicePedido.nuevoPedido(_cliente, nuevoPedidoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    //Devuelve una lista con todos los pedidos para ser gestionados por el cajero
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'CAJERO')")
    @GetMapping("/cajero")
    public Page<PedidoCajeroDto> mostrarPedidosCajero(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "9") int size){
        return servicePedido.mostrarPedidosCajero(page,size);
    }

    //Devuelve un arreglo con todos los pedidos EN_PREPARACION y LISTO para ser gestionados por el cocinero
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'COCINERO')")
    @GetMapping("/cocinero")
    public ResponseEntity<ArregloPedidosCocinaDto> mostrarPedidosCocinero(){
        return ResponseEntity.ok(servicePedido.mostrarPedidosCocinero());
    }

    //Devuelve un arreglo con todos los pedidos correspondientes para el repartidor
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'REPARTIDOR')")
    @GetMapping("/repartidor")
    public ResponseEntity<ArregloPedidosRepartidorDto> mostrarPedidosRepartidor(){
        return ResponseEntity.ok(servicePedido.mostrarPedidosRepartidor());
    }

    //El cliente cancela el pedido
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'CLIENTE')")
    @PutMapping("/cancelado/{idPedido}")
    public ResponseEntity<String> cancelarPedido(@PathVariable Long idPedido) {
        servicePedido.cancelarPedido(idPedido);
        return ResponseEntity.ok("Pedido cancelado correctamente");
    }

    //El cajero confirma el pedido y pasa al estado 'EN_PREPARACION'
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'CAJERO')")
    @PutMapping("/confirmado/{idPedido}")
    public ResponseEntity<String> confirmarPedido(@PathVariable Long idPedido) {
        servicePedido.confirmarPedido(idPedido);
        return ResponseEntity.ok("El pedido fue confirmado correctamente");
    }

    //El cajero rechaza el pedido
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'CAJERO')")
    @PutMapping("/rechazado/{idPedido}")
    public ResponseEntity<String> rechazarPedido(@PathVariable Long idPedido) {
        servicePedido.rechazarPedido(idPedido);
        return ResponseEntity.ok("El pedido fue rechazado de manera exitosa");
    }

    //El cocinero marca el pedido como 'listo'
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'COCINERO')")
    @PutMapping("/listo/{idPedido}")
    public ResponseEntity<String> pedidoListoParaEntrega(@PathVariable Long idPedido){
        servicePedido.pedidoListo(idPedido);
        return ResponseEntity.ok("El pedido fue marcado como LISTO");
    }

    //El repartidor marca el pedido como retirado del local y pasa a estodo EN_CAMINO
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'REPARTIDOR')")
    @PutMapping("/enCamino/{idPedido}")
    public ResponseEntity<String> pedidoRetirado(@PathVariable Long idPedido){
        servicePedido.pedidoRetiradoDelLocal(idPedido);
        return ResponseEntity.ok("El pedido está en camino");
    }

    //El repartidor o el cajero marca el pedido como entregado
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'REPARTIDOR', 'CAJERO')")
    @PutMapping("/entregado/{idPedido}")
    public ResponseEntity<String> pedidoEstregado(@PathVariable Long idPedido){
        servicePedido.pedidoEntregadoAlCliente(idPedido);
        serviceFactura.enviarFacturaPorMail(idPedido);
        return ResponseEntity.ok("El pedido fue entregado al cliente");
    }

    //El cocinero puede extender en 5 minutos el horario de entrega del pedido si este se encuentra demorado
    //@PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'COCINERO')")
    @PutMapping(("/extenderHorarioEntrega/{idPedido}"))
    public ResponseEntity<String> extenderDemoraPedido(@PathVariable Long idPedido){
        servicePedido.extenderDemoraPedido(idPedido);
        return ResponseEntity.ok("Se añadieron 5 minutos al horario de entrega del pedido");
    }

    //Devuelve el historial de pedidos realizados por un cliente
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'CLIENTE')")
    @GetMapping("/historialCliente/{idCliente}")
    public ResponseEntity<HistorialDePedidosDto> pedidosRealizados(@PathVariable Long idCliente){
        HistorialDePedidosDto historialDePedidosDto = servicePedido.mostrarHistorialDePedidos(idCliente);

        return ResponseEntity.ok(historialDePedidosDto);
    }
}
