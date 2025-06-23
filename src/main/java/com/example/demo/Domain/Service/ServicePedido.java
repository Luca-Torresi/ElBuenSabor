package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Articulo.ArticuloDto;
import com.example.demo.Application.DTO.Pedido.*;
import com.example.demo.Application.Mapper.PedidoMapper;
import com.example.demo.Domain.Entities.*;
import com.example.demo.Domain.Enums.EstadoPedido;
import com.example.demo.Domain.Enums.MetodoDePago;
import com.example.demo.Domain.Enums.TipoEnvio;
import com.example.demo.Domain.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final RepoPedidoPendiente repoPedidoPendiente;
    private final PedidoMapper pedidoMapper;
    private final RepoArticulo repoArticulo;

    //Luego de evaluar si existen insumos suficientes, persiste el pedido en la base de datos
    public String generarNuevoPedido(OidcUser _cliente, NuevoPedidoDto nuevoPedidoDto) {
        String _faltantes = evaluarStock(nuevoPedidoDto);

        if(_faltantes.equals("")){

            //String email = _cliente.getEmail();
            //Optional<Cliente> clienteOpt = repoCliente.findByEmail(email);
            TipoEnvio tipoEnvio = TipoEnvio.valueOf(nuevoPedidoDto.getTipoEnvio());
            MetodoDePago metodoDePago = MetodoDePago.valueOf(nuevoPedidoDto.getMetodoDePago());

            Pedido pedido = Pedido.builder()
                    //.cliente(clienteOpt.get())
                    .tipoEnvio(tipoEnvio)
                    .estadoPedido(EstadoPedido.A_CONFIRMAR)
                    .metodoDePago(metodoDePago)
                    .fechaYHora(LocalDateTime.now())
                    .build();

            List<DetallePedido> detalles = new ArrayList<>();
            for(NuevoDetallePedidoDto detalle : nuevoPedidoDto.getDetalles()){
                Optional<Articulo> articuloOpt = repoArticulo.findById(detalle.getIdArticulo());

                DetallePedido detallePedido = DetallePedido.builder()
                        .articulo(articuloOpt.get())
                        .cantidad(detalle.getCantidad())
                        .subtotal(articuloOpt.get().getPrecioVenta() * detalle.getCantidad())
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

    //Evalua si hay stock suficiente para la elaboración del pedido
    @Transactional
    public String evaluarStock(NuevoPedidoDto nuevoPedidoDto) {
        PedidoPendiente pedidoPendiente = PedidoPendiente.builder()
                .build();

        List<DetallePedidoPendiente> detalles = new ArrayList<>();
        for(NuevoDetallePedidoDto detalle : nuevoPedidoDto.getDetalles()){
            Optional<Articulo> articuloOpt = repoArticulo.findById(detalle.getIdArticulo());

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

    //Devuelve una lista con todos los pedidos realizados al local
    public Page<PedidoDto> listarTodosLosPedidos(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Pedido> paginaPedidos = repoPedido.findAll(pageable);

        return paginaPedidos.map(pedido -> {
            PedidoDto dto = pedidoMapper.pedidoToPedidoDto(pedido);
            return dto;
        });
    }

    //El cliente cancela el pedido
    public void cancelarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido).get();
        pedido.setEstadoPedido(EstadoPedido.CANCELADO);
        repoPedido.save(pedido);
    }

    //El cajero confirma el pedido
    public void confirmarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido).get();
        pedido.setEstadoPedido(EstadoPedido.EN_PREPARACION);
        repoPedido.save(pedido);
    }

    //El cajero rechaza el pedido
    public void rechazarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido).get();
        pedido.setEstadoPedido(EstadoPedido.RECHAZADO);
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
        List<NuevoPedidoDto> pedidosRealizados = new ArrayList<>();

        List<Pedido> pedidos = repoPedido.findPedidosByIdCliente(idCliente);
        for(Pedido pedido : pedidos){
            NuevoPedidoDto nuevoPedidoDto = pedidoMapper.pedidoToNuevoPedidoDto(pedido);
            pedidosRealizados.add(nuevoPedidoDto);
        }
        historialDePedidosDto.setPedidos(pedidosRealizados);

        return historialDePedidosDto;
    }
}
