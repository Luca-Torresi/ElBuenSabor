package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Pedido.CambioDeEstadoDto;
import com.example.demo.Application.DTO.Pedido.DetallePedidoDto;
import com.example.demo.Application.DTO.Pedido.HistorialDePedidosDto;
import com.example.demo.Application.DTO.Pedido.PedidoDto;
import com.example.demo.Application.Mapper.PedidoMapper;
import com.example.demo.Domain.Entities.*;
import com.example.demo.Domain.Enums.EstadoPedido;
import com.example.demo.Domain.Enums.MetodoDePago;
import com.example.demo.Domain.Enums.TipoEnvio;
import com.example.demo.Domain.Repositories.RepoArticuloManufacturado;
import com.example.demo.Domain.Repositories.RepoCliente;
import com.example.demo.Domain.Repositories.RepoPedido;
import com.example.demo.Domain.Repositories.RepoPedidoPendiente;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicePedido {
    private final RepoPedido repoPedido;
    private final RepoCliente repoCliente;
    private final RepoArticuloManufacturado repoArticuloManufacturado;
    private final RepoPedidoPendiente repoPedidoPendiente;
    private final PedidoMapper pedidoMapper;

    //Luego de evaluar si existen insumos suficientes, persiste el pedido en la base de datos
    public String generarNuevoPedido(OidcUser _cliente, PedidoDto pedidoDto) {
        String _faltantes = evaluarStock(pedidoDto);

        if(_faltantes.equals("")){

            String email = _cliente.getEmail();
            Optional<Cliente> clienteOpt = repoCliente.findByEmail(email);
            TipoEnvio tipoEnvio = TipoEnvio.valueOf(pedidoDto.getTipoEnvio());
            MetodoDePago metodoDePago = MetodoDePago.valueOf(pedidoDto.getMetodoDePago());

            Pedido pedido = Pedido.builder()
                    .cliente(clienteOpt.get())
                    .tipoEnvio(tipoEnvio)
                    .estadoPedido(EstadoPedido.A_CONFIRMAR)
                    .metodoDePago(metodoDePago)
                    .fechaYHora(LocalDateTime.now())
                    .build();

            List<DetallePedido> detalles = new ArrayList<>();
            for(DetallePedidoDto detalle : pedidoDto.getDetalles()){
                Optional<ArticuloManufacturado> articuloOpt = repoArticuloManufacturado.findById(detalle.getIdArticulo());

                DetallePedido detallePedido = DetallePedido.builder()
                        .articulo(articuloOpt.get())
                        .cantidad(detalle.getCantidad())
                        .pedido(pedido)
                        .build();
                detalles.add(detallePedido);
            }
            pedido.setDetalles(detalles);

            repoPedido.save(pedido);

            return "Todo ok";
        } else {
            return _faltantes;
        }
    }

    //Antes de persistir el pedido en la base de datos, evalua si hay stock suficiente para su elaboración
    public String evaluarStock(PedidoDto pedidoDto) {
        PedidoPendiente pedidoPendiente = PedidoPendiente.builder()
                .build();

        List<DetallePedidoPendiente> detalles = new ArrayList<>();
        for(DetallePedidoDto detalle : pedidoDto.getDetalles()){
            Optional<ArticuloManufacturado> articuloOpt = repoArticuloManufacturado.findById(detalle.getIdArticulo());

            DetallePedidoPendiente detallePedidoPendiente = DetallePedidoPendiente.builder()
                    .articulo(articuloOpt.get())
                    .cantidad(detalle.getCantidad())
                    .pedidoPendiente(pedidoPendiente)
                    .build();
            detalles.add(detallePedidoPendiente);
        }
        pedidoPendiente.setDetalles(detalles);

        PedidoPendiente nuevoPedido = repoPedidoPendiente.save(pedidoPendiente);
        return repoPedidoPendiente.evaluarStockParaPedido(nuevoPedido.getIdPedidoPendiente());
    }

    //El cliente cancela el pedido
    public void cancelarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido).get();

        pedido.setEstadoPedido(EstadoPedido.CANCELADO);
        repoPedido.save(pedido);
    }

    //El cajero puede aceptar o rechazar el pedido
    public void cambiarEstado(CambioDeEstadoDto cambioDeEstadoDto) {
        Pedido pedido = repoPedido.findById(cambioDeEstadoDto.getIdPedido()).get();

        pedido.setEstadoPedido(EstadoPedido.valueOf(cambioDeEstadoDto.getEstadoPedido()));
        repoPedido.save(pedido);
    }

    //El cocinero marca el pedido como listo
    public void pedidoListo(Long idPedido){
        Pedido pedido = repoPedido.findById(idPedido).get();

        pedido.setEstadoPedido(EstadoPedido.LISTO);
        repoPedido.save(pedido);
    }

    //Devuelve un arreglo con todos los pedidos realizados por el cliente
    public HistorialDePedidosDto mostrarHistorialDePedidos(Long idCliente){
        HistorialDePedidosDto historialDePedidosDto = new HistorialDePedidosDto();
        List<PedidoDto> pedidosRealizados = new ArrayList<>();

        List<Pedido> pedidos = repoPedido.findPedidosByIdCliente(idCliente);
        for(Pedido pedido : pedidos){
            PedidoDto pedidoDto = pedidoMapper.pedidoToPedidoDto(pedido);
            pedidosRealizados.add(pedidoDto);
        }
        historialDePedidosDto.setPedidos(pedidosRealizados);

        return historialDePedidosDto;
    }
}
