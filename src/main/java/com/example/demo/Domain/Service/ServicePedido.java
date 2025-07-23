package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Pedido.*;
import com.example.demo.Application.Mapper.PedidoMapper;
import com.example.demo.Domain.Entities.*;
import com.example.demo.Domain.Enums.EstadoPedido;
import com.example.demo.Domain.Enums.MetodoDePago;
import com.example.demo.Domain.Enums.TipoEnvio;
import com.example.demo.Domain.Repositories.*;
import com.example.demo.Domain.Exceptions.InsumosInsuficientesException;
import com.example.demo.Domain.Exceptions.PedidoNoEncontradoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ServicePedido {
    private final RepoPedido repoPedido;
    private final RepoCliente repoCliente;
    private final PedidoMapper pedidoMapper;
    private final RepoArticulo repoArticulo;
    private final RepoArticuloInsumo repoArticuloInsumo;
    private final RepoArticuloManufacturado repoArticuloManufacturado;
    private final RepoArticuloNoElaborado repoArticuloNoElaborado;
    private final RepoDireccion repoDireccion;

    //Luego de verificar que existan insumos suficientes para su elaboración, se persiste el pedido en la base de datos
    public Pedido nuevoPedido(OidcUser _cliente, NuevoPedidoDto nuevoPedidoDto) {
        if (!evaluarStock(nuevoPedidoDto)) {
            throw new InsumosInsuficientesException("No hay insumos suficientes para la elaboración del pedido");
        }

        /*
        Cliente cliente = repoCliente.findByEmail(_cliente.getEmail())
                .orElseThrow(() -> new ClienteNoEncontradoException("No se encontró el cliente en la base de datos"));
        */

        TipoEnvio tipoEnvio = TipoEnvio.valueOf(nuevoPedidoDto.getTipoEnvio());
        MetodoDePago metodoDePago = MetodoDePago.valueOf(nuevoPedidoDto.getMetodoDePago());

        Pedido pedido = Pedido.builder()
                //.cliente(cliente)
                .tipoEnvio(tipoEnvio)
                .estadoPedido(EstadoPedido.A_CONFIRMAR)
                .metodoDePago(metodoDePago)
                .fechaYHora(LocalDateTime.now())
                .build();

        if(nuevoPedidoDto.getIdDireccion() != null){
            Direccion direccion = repoDireccion.findById(nuevoPedidoDto.getIdDireccion()).get();
            pedido.setDireccion(direccion);
        }

        int demora = 0;
        double total = 0;

        List<DetallePedido> detalles = new ArrayList<>();
        for (NuevoDetallePedidoDto detalle : nuevoPedidoDto.getDetalles()) {
            Articulo articulo = repoArticulo.findById(detalle.getIdArticulo()).get();

            if(articulo.isEsManufacturado()){
                ArticuloManufacturado articuloManufacturado = repoArticuloManufacturado.findById(detalle.getIdArticulo()).get();
                if(articuloManufacturado.getTiempoDeCocina() > demora){
                    demora = articuloManufacturado.getTiempoDeCocina();
                }
            }

            DetallePedido detallePedido = DetallePedido.builder()
                    .articulo(articulo)
                    .cantidad(detalle.getCantidad())
                    .subtotal(articulo.getPrecioVenta() * detalle.getCantidad())
                    .pedido(pedido)
                    .build();
            detalles.add(detallePedido);

            total += detallePedido.getSubtotal();
        }
        pedido.setDetalles(detalles);
        pedido.setTotal(total);

        //Establecemos el horario de entrega del pedido
        //Si el tipo de envío es DELIVERY se suman 15 minutos a la demora
        if(pedido.getTipoEnvio().equals(TipoEnvio.DELIVERY)){
            pedido.setHoraEntrega(pedido.getFechaYHora().plusMinutes(demora + 15));
        } else{
            pedido.setHoraEntrega(pedido.getFechaYHora().plusMinutes(demora));
        }

        return repoPedido.save(pedido);
    }

    //Evalua si hay stock suficiente para la elaboración del pedido
    public boolean evaluarStock(NuevoPedidoDto nuevoPedidoDto) {
        Map<ArticuloInsumo, Double> stockDisponible = new HashMap<>();
        for (ArticuloInsumo insumo : repoArticuloInsumo.findAll()) {
            stockDisponible.put(insumo, insumo.getStockActual());
        }

        for (NuevoDetallePedidoDto detallePedido : nuevoPedidoDto.getDetalles()) {
            Articulo articulo = repoArticulo.findById(detallePedido.getIdArticulo()).get();

            if (articulo.isEsManufacturado()) {
                ArticuloManufacturado articuloManufacturado = repoArticuloManufacturado.findById(detallePedido.getIdArticulo()).get();

                for (ArticuloManufacturadoDetalle detalle : articuloManufacturado.getDetalles()) {
                    ArticuloInsumo insumo = detalle.getArticuloInsumo();
                    Double nuevoStock = stockDisponible.get(insumo) - detalle.getCantidad() * detallePedido.getCantidad();

                    if (nuevoStock < 0) {
                        return false;
                    } else {
                        stockDisponible.put(insumo, nuevoStock);
                    }
                }

            } else {
                ArticuloNoElaborado articuloNoElaborado = repoArticuloNoElaborado.findById(detallePedido.getIdArticulo()).get();
                if (articuloNoElaborado.getStock() - detallePedido.getCantidad() < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    //Devuelve una lista paginada con todos los pedidos realizados al restaurante
    public Page<PedidoCajeroDto> mostrarPedidosCajero(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Pedido> paginaPedidos = repoPedido.findAll(pageable);

        return paginaPedidos.map(pedido -> {
            PedidoCajeroDto dto = pedidoMapper.pedidoToPedidoDto(pedido);
            return dto;
        });
    }

    //Obtiene de la base de datos todos los pedidos que se encuentran en preparación o listos para la entrega
    public ArregloPedidosCocinaDto mostrarPedidosCocinero() {
        List<EstadoPedido> estados = List.of(EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO);
        List<Pedido> pedidos = repoPedido.findByEstadoPedidoIn(estados);

        ArregloPedidosCocinaDto arregloPedidosCocinaDto = new ArregloPedidosCocinaDto();
        List<PedidoCocinaDto> pedidosDto = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            PedidoCocinaDto dto = pedidoMapper.pedidoToPedidoCocinaDto(pedido);
            pedidosDto.add(dto);
        }
        arregloPedidosCocinaDto.setPedidos(pedidosDto);
        return arregloPedidosCocinaDto;
    }

    //Obtiene los pedidos correspondientes para el repartidor
    public ArregloPedidosRepartidorDto mostrarPedidosRepartidor(){
        List<EstadoPedido> estados = List.of(EstadoPedido.LISTO, EstadoPedido.EN_CAMINO);
        List<Pedido> pedidos = repoPedido.findByEstadoPedidoInAndTipoEnvio(estados, TipoEnvio.DELIVERY);

        ArregloPedidosRepartidorDto arregloPedidosRepartidorDto = new ArregloPedidosRepartidorDto();
        List<PedidoRepartidorDto> pedidosDto = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            PedidoRepartidorDto dto = pedidoMapper.pedidoToPedidoRepartidorDto(pedido);
            pedidosDto.add(dto);
        }
        arregloPedidosRepartidorDto.setPedidos(pedidosDto);
        return arregloPedidosRepartidorDto;
    }

    //El cliente cancela el pedido
    public void cancelarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setEstadoPedido(EstadoPedido.CANCELADO);
        repoPedido.save(pedido);
    }

    //El cajero confirma el pedido
    public void confirmarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setEstadoPedido(EstadoPedido.EN_PREPARACION);
        repoPedido.save(pedido);
    }

    //El cajero rechaza el pedido
    public void rechazarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setEstadoPedido(EstadoPedido.RECHAZADO);
        repoPedido.save(pedido);
    }

    //El cocinero marca el pedido como listo
    public void pedidoListo(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setEstadoPedido(EstadoPedido.LISTO);
        repoPedido.save(pedido);
    }

    //El pedido cambia al estado EN_CAMINO
    public void pedidoRetiradoDelLocal(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setEstadoPedido(EstadoPedido.EN_CAMINO);
        repoPedido.save(pedido);
    }

    //El pedido cambia al estado ENTREGADO
    @Transactional
    public void pedidoEntregadoAlCliente(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setEstadoPedido(EstadoPedido.ENTREGADO);

        if(pedido.getTotal() == null) {
            pedido.setTotal((double) 0);
        }
        repoPedido.save(pedido);
    }

    //Añade 5 minutos al horario de entrega de un pedido
    public void extenderDemoraPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setHoraEntrega(pedido.getHoraEntrega().plusMinutes(5));
        repoPedido.save(pedido);
    }

    //Devuelve un arreglo con todos los pedidos realizados por el cliente
    public HistorialDePedidosDto mostrarHistorialDePedidos(Long idCliente) {
        HistorialDePedidosDto historialDePedidosDto = new HistorialDePedidosDto();
        List<NuevoPedidoDto> pedidosRealizados = new ArrayList<>();

        List<Pedido> pedidos = repoPedido.findPedidosByIdCliente(idCliente);
        for (Pedido pedido : pedidos) {
            NuevoPedidoDto nuevoPedidoDto = pedidoMapper.pedidoToNuevoPedidoDto(pedido);
            pedidosRealizados.add(nuevoPedidoDto);
        }
        historialDePedidosDto.setPedidos(pedidosRealizados);

        return historialDePedidosDto;
    }
}
