package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Pedido.*;
import com.example.demo.Application.DTO.Pedido.Cajero.PedidoCajeroDto;
import com.example.demo.Application.DTO.Pedido.Cliente.NuevoDetallePedidoDto;
import com.example.demo.Application.DTO.Pedido.Cliente.NuevoPedidoDto;
import com.example.demo.Application.DTO.Pedido.Cliente.PedidoClienteDto;
import com.example.demo.Application.DTO.Pedido.Cocinero.PedidoCocinaDto;
import com.example.demo.Application.DTO.Pedido.Repartidor.PedidoRepartidorDto;
import com.example.demo.Application.Mapper.PedidoMapper;
import com.example.demo.Domain.Entities.*;
import com.example.demo.Domain.Enums.EstadoPedido;
import com.example.demo.Domain.Enums.MetodoDePago;
import com.example.demo.Domain.Enums.TipoEnvio;
import com.example.demo.Domain.Exceptions.ClienteNoEncontradoException;
import com.example.demo.Domain.Repositories.*;
import com.example.demo.Domain.Exceptions.InsumosInsuficientesException;
import com.example.demo.Domain.Exceptions.PedidoNoEncontradoException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
@RequiredArgsConstructor
public class ServicePedido {
    private final RepoPedido repoPedido;
    private final RepoUsuario repoUsuario;
    private final RepoCliente repoCliente;
    private final PedidoMapper pedidoMapper;
    private final RepoArticulo repoArticulo;
    private final RepoArticuloInsumo repoArticuloInsumo;
    private final RepoArticuloManufacturado repoArticuloManufacturado;
    private final RepoArticuloNoElaborado repoArticuloNoElaborado;
    private final RepoDireccion repoDireccion;
    private final SimpMessagingTemplate messagingTemplate;
    private final RepoPromocion repoPromocion;

    //Luego de verificar que existan insumos suficientes para su elaboración, se persiste el pedido en la base de datos
    public Pedido nuevoPedido(String _cliente, NuevoPedidoDto nuevoPedidoDto) {
        if (!evaluarStock(nuevoPedidoDto)) {
            throw new InsumosInsuficientesException("No hay insumos suficientes para la elaboración del pedido");
        }

        Cliente cliente = repoCliente.findByIdAuth0(_cliente)
                .orElseThrow(() -> new ClienteNoEncontradoException("No se encontró el cliente en la base de datos"));

        TipoEnvio tipoEnvio = TipoEnvio.valueOf(nuevoPedidoDto.getTipoEnvio());
        MetodoDePago metodoDePago = MetodoDePago.valueOf(nuevoPedidoDto.getMetodoDePago());

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .tipoEnvio(tipoEnvio)
                .estadoPedido(EstadoPedido.A_CONFIRMAR)
                .metodoDePago(metodoDePago)
                .fechaYHora(LocalDateTime.now())
                .build();

        if(nuevoPedidoDto.getIdDireccion() != null){
            Direccion direccion = repoDireccion.findById(nuevoPedidoDto.getIdDireccion()).get();
            pedido.setDireccion(direccion);
        }

        Double total = 0.0;
        Integer demora = 0, max = 0, suma = 0, tiempo = 0;
        Double penalizacion = 0.3;

        List<DetallePedido> detalles = new ArrayList<>();
        for (NuevoDetallePedidoDto detalle : nuevoPedidoDto.getDetalles()) {
            if(detalle.getIdPromocion() == null){
                Articulo articulo = repoArticulo.findById(detalle.getIdArticulo()).get();

                DetallePedido detallePedido = DetallePedido.builder()
                        .articulo(articulo)
                        .cantidad(detalle.getCantidad())
                        .subtotal(articulo.getPrecioVenta() * detalle.getCantidad())
                        .pedido(pedido)
                        .build();
                detalles.add(detallePedido);

                if(articulo.getEsManufacturado()){
                    ArticuloManufacturado articuloManufacturado = repoArticuloManufacturado.findById(detalle.getIdArticulo()).get();
                    tiempo = articuloManufacturado.getTiempoDeCocina();

                    suma += tiempo;
                }

                total += detallePedido.getSubtotal();

            } else if(detalle.getIdArticulo() == null){
                Promocion promocion = repoPromocion.findById(detalle.getIdPromocion()).get();

                DetallePedido detallePedido = DetallePedido.builder()
                        .promocion(promocion)
                        .cantidad(detalle.getCantidad())
                        .subtotal(promocion.getPrecio() * detalle.getCantidad())
                        .pedido(pedido)
                        .build();
                detalles.add(detallePedido);

                tiempo = promocion.getTiempoDeCocina();
                suma += tiempo;

                total += detallePedido.getSubtotal();
            }

            if(tiempo > max){
                max = tiempo;
            }
        }
        pedido.setDetalles(detalles);

        if(pedido.getTipoEnvio() == TipoEnvio.DELIVERY){
            total += 2000;
        }
        pedido.setTotal(total);

        //Si el tipo de envío es DELIVERY se suman 15 minutos a la demora
        demora = (int) Math.round((max + suma * penalizacion) / 2.0);
        if(pedido.getTipoEnvio().equals(TipoEnvio.DELIVERY)){
            pedido.setHoraEntrega(pedido.getFechaYHora().plusMinutes(demora + 15));
        } else{
            pedido.setHoraEntrega(pedido.getFechaYHora().plusMinutes(demora));
        }

        Pedido savedPedido = repoPedido.save(pedido);

        // --- Notificación WebSocket para nuevo pedido ---
        PedidoStatusUpdateDto newOrderDto = PedidoStatusUpdateDto.builder()
                .idPedido(savedPedido.getIdPedido())
                .estadoPedido(savedPedido.getEstadoPedido())
                .clienteId(savedPedido.getCliente().getIdUsuario())
                .horaEntrega(savedPedido.getHoraEntrega())
                .mensajeActualizacion("Nuevo pedido recibido.")
                .build();

        // Notificar al cliente específico sobre su pedido
        messagingTemplate.convertAndSend("/topic/pedido/updates/" + pedido.getIdPedido(), newOrderDto);
        // Notificar al panel de cajero sobre un nuevo pedido
        messagingTemplate.convertAndSend("/topic/cashier/orders", newOrderDto);
        // -------------------------------------------------

        return savedPedido;
    }

    //Evalua si hay stock suficiente para la elaboración del pedido
    public boolean evaluarStock(NuevoPedidoDto nuevoPedidoDto) {
        Map<ArticuloInsumo, Double> stockInsumo = new HashMap<>();
        for (ArticuloInsumo insumo : repoArticuloInsumo.findAll()) {
            stockInsumo.put(insumo, insumo.getStockActual());
        }

        Map<ArticuloNoElaborado, Integer> stockNoElaborado = new HashMap<>();
        for(ArticuloNoElaborado articuloNoElaborado: repoArticuloNoElaborado.findAll()){
            stockNoElaborado.put(articuloNoElaborado, articuloNoElaborado.getStock());
        }

        boolean valor;
        for (NuevoDetallePedidoDto detallePedido : nuevoPedidoDto.getDetalles()) {
            if(detallePedido.getIdPromocion() == null){
                valor = evaluarStock2(detallePedido.getIdArticulo(), detallePedido.getCantidad(), stockInsumo, stockNoElaborado);
                if(valor == false){
                    return false;
                }
            } else {
                Promocion promocion = repoPromocion.findById(detallePedido.getIdPromocion()).get();

                for(DetallePromocion detalles: promocion.getDetalles()){
                    valor = evaluarStock2(detalles.getArticulo().getIdArticulo(), detalles.getCantidad(), stockInsumo, stockNoElaborado);
                    if(valor == false){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean evaluarStock2(Long idArticulo, Integer cantidad, Map<ArticuloInsumo, Double> stockInsumo, Map<ArticuloNoElaborado, Integer> stockNoElaborado){
        Articulo articulo = repoArticulo.findById(idArticulo).get();

        if (articulo.getEsManufacturado()) {
            ArticuloManufacturado articuloManufacturado = repoArticuloManufacturado.findById(idArticulo).get();

            for (ArticuloManufacturadoDetalle detalle : articuloManufacturado.getDetalles()) {
                ArticuloInsumo insumo = detalle.getArticuloInsumo();
                Double nuevoStock = stockInsumo.get(insumo) - detalle.getCantidad() * cantidad;

                if (nuevoStock < 0) {
                    return false;
                } else {
                    stockInsumo.put(insumo, nuevoStock);
                }
            }

        } else {
            ArticuloNoElaborado articuloNoElaborado = repoArticuloNoElaborado.findById(idArticulo).get();

            Integer nuevoStock = stockNoElaborado.get(articuloNoElaborado) - cantidad;
            if(nuevoStock < 0){
                return false;
            } else {
                stockNoElaborado.put(articuloNoElaborado, nuevoStock);
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
    public List<PedidoCocinaDto> mostrarPedidosCocinero() {
        return repoPedido.findByEstadoPedidoIn(List.of(EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO))
                .stream()
                .map(pedidoMapper::pedidoToPedidoCocinaDto)
                .toList();
    }

    //Obtiene los pedidos correspondientes para el repartidor
    public List<PedidoRepartidorDto> mostrarPedidosRepartidor(){
        return repoPedido.findByEstadoPedidoIn(List.of(EstadoPedido.LISTO, EstadoPedido.EN_CAMINO))
                .stream()
                .map(pedidoMapper::pedidoToPedidoRepartidorDto)
                .toList();
    }

    //El cliente cancela el pedido
    public void cancelarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setEstadoPedido(EstadoPedido.CANCELADO);
        repoPedido.save(pedido);

        // --- Notificación WebSocket ---
        PedidoStatusUpdateDto updateDto = PedidoStatusUpdateDto.builder()
                .idPedido(pedido.getIdPedido())
                .estadoPedido(pedido.getEstadoPedido())
                .clienteId(pedido.getCliente().getIdUsuario())
                .mensajeActualizacion("Pedido cancelado por el cliente.")
                .build();
        messagingTemplate.convertAndSend("/topic/pedido/updates/" + pedido.getIdPedido(), updateDto);
        messagingTemplate.convertAndSend("/topic/cashier/orders", updateDto); // Notificar al cajero
        // ------------------------------
    }

    //El cajero confirma el pedido
    public void confirmarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setEstadoPedido(EstadoPedido.EN_PREPARACION);
        repoPedido.save(pedido);

        // --- Notificación WebSocket ---
        PedidoStatusUpdateDto updateDto = PedidoStatusUpdateDto.builder()
                .idPedido(pedido.getIdPedido())
                .estadoPedido(pedido.getEstadoPedido())
                .clienteId(pedido.getCliente().getIdUsuario())
                .horaEntrega(pedido.getHoraEntrega()) // Incluir hora de entrega
                .mensajeActualizacion("Pedido confirmado y en preparación.")
                .build();
        messagingTemplate.convertAndSend("/topic/pedido/updates/" + pedido.getIdPedido(), updateDto);
        messagingTemplate.convertAndSend("/topic/cashier/orders", updateDto); // Notificar al cajero
        messagingTemplate.convertAndSend("/topic/kitchen/orders", updateDto); // Notificar a la cocina
        // ------------------------------
    }

    //El cajero rechaza el pedido
    public void rechazarPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setEstadoPedido(EstadoPedido.RECHAZADO);
        repoPedido.save(pedido);

        // --- Notificación WebSocket ---
        PedidoStatusUpdateDto updateDto = PedidoStatusUpdateDto.builder()
                .idPedido(pedido.getIdPedido())
                .estadoPedido(pedido.getEstadoPedido())
                .clienteId(pedido.getCliente().getIdUsuario())
                .mensajeActualizacion("Pedido rechazado por el cajero.")
                .build();
        messagingTemplate.convertAndSend("/topic/pedido/updates/" + pedido.getIdPedido(), updateDto);
        messagingTemplate.convertAndSend("/topic/cashier/orders", updateDto); // Notificar al cajero
        // ------------------------------
    }

    //El cocinero marca el pedido como listo
    public void pedidoListo(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setEstadoPedido(EstadoPedido.LISTO);
        repoPedido.save(pedido);
        // --- Notificación WebSocket ---
        PedidoStatusUpdateDto updateDto = PedidoStatusUpdateDto.builder()
                .idPedido(pedido.getIdPedido())
                .estadoPedido(pedido.getEstadoPedido())
                .clienteId(pedido.getCliente().getIdUsuario())
                .horaEntrega(pedido.getHoraEntrega()) // Incluir hora de entrega
                .mensajeActualizacion("Pedido listo para entrega.")
                .build();
        messagingTemplate.convertAndSend("/topic/pedido/updates/" + pedido.getIdPedido(), updateDto);
        messagingTemplate.convertAndSend("/topic/cashier/orders", updateDto); // Notificar al cajero
        messagingTemplate.convertAndSend("/topic/kitchen/orders", updateDto); // Notificar a la cocina
        messagingTemplate.convertAndSend("/topic/delivery/orders", updateDto); // Notificar al repartidor
        // ------------------------------
    }

    //El pedido cambia al estado EN_CAMINO
    public void pedidoRetiradoDelLocal(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setEstadoPedido(EstadoPedido.EN_CAMINO);
        repoPedido.save(pedido);

        // --- Notificación WebSocket ---
        PedidoStatusUpdateDto updateDto = PedidoStatusUpdateDto.builder()
                .idPedido(pedido.getIdPedido())
                .estadoPedido(pedido.getEstadoPedido())
                .clienteId(pedido.getCliente().getIdUsuario())
                .horaEntrega(pedido.getHoraEntrega()) // Incluir hora de entrega
                .mensajeActualizacion("Pedido retirado del local, en camino.")
                .build();
        messagingTemplate.convertAndSend("/topic/pedido/updates/" + pedido.getIdPedido(), updateDto);
        messagingTemplate.convertAndSend("/topic/cashier/orders", updateDto); // Notificar al cajero
        messagingTemplate.convertAndSend("/topic/delivery/orders", updateDto); // Notificar al repartidor
        // ------------------------------
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

        // --- Notificación WebSocket ---
        PedidoStatusUpdateDto updateDto = PedidoStatusUpdateDto.builder()
                .idPedido(pedido.getIdPedido())
                .estadoPedido(pedido.getEstadoPedido())
                .clienteId(pedido.getCliente().getIdUsuario())
                .mensajeActualizacion("Pedido entregado al cliente.")
                .build();
        messagingTemplate.convertAndSend("/topic/pedido/updates/" + pedido.getIdPedido(), updateDto);
        messagingTemplate.convertAndSend("/topic/cashier/orders", updateDto); // Notificar al cajero
        messagingTemplate.convertAndSend("/topic/delivery/orders", updateDto); // Notificar al repartidor
        // ------------------------------
    }

    //Añade 5 minutos al horario de entrega de un pedido
    public void extenderDemoraPedido(Long idPedido) {
        Pedido pedido = repoPedido.findById(idPedido)
                .orElseThrow(() -> new PedidoNoEncontradoException("No se encontró el pedido con ID " + idPedido));

        pedido.setHoraEntrega(pedido.getHoraEntrega().plusMinutes(5));
        repoPedido.save(pedido);

        // --- Notificación WebSocket ---
        PedidoStatusUpdateDto updateDto = PedidoStatusUpdateDto.builder()
                .idPedido(pedido.getIdPedido())
                .estadoPedido(pedido.getEstadoPedido())
                .clienteId(pedido.getCliente().getIdUsuario())
                .horaEntrega(pedido.getHoraEntrega()) // Es crucial enviar la nueva hora de entrega
                .mensajeActualizacion("Tiempo de preparación extendido en 5 minutos.")
                .build();
        messagingTemplate.convertAndSend("/topic/pedido/updates/" + pedido.getIdPedido(), updateDto);
        messagingTemplate.convertAndSend("/topic/cashier/orders", updateDto); // Notificar al cajero
        messagingTemplate.convertAndSend("/topic/kitchen/orders", updateDto); // Notificar a la cocina
        // ------------------------------
    }

    //Devuelve un arreglo con todos los pedidos realizados por el cliente
    public List<PedidoClienteDto> mostrarHistorialDePedidos(Long idCliente) {
        return repoPedido.findPedidosByIdCliente(idCliente)
                .stream()
                .map(pedidoMapper::pedidoToPedidoClienteDto)
                .toList();
    }

    public Page<PedidoClienteDto> mostrarPedidosEnCursoCliente(String idAuth0, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Long idCliente = getIdUsuarioDesdeAuth0(idAuth0);

        List<EstadoPedido> estadosEnCurso = List.of(
                EstadoPedido.A_CONFIRMAR,
                EstadoPedido.EN_PREPARACION,
                EstadoPedido.LISTO,
                EstadoPedido.EN_CAMINO
        );

        Page<Pedido> paginaPedidos = repoPedido.findByCliente_IdUsuarioAndEstadoPedidoIn(idCliente, estadosEnCurso, pageable);

        return paginaPedidos.map(pedidoMapper::pedidoToPedidoClienteDto);
    }

    public Page<PedidoClienteDto> mostrarHistorialPedidosCliente(String idAuth0, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Long idCliente = getIdUsuarioDesdeAuth0(idAuth0);

        List<EstadoPedido> estadosHistorial = List.of(
                EstadoPedido.ENTREGADO,
                EstadoPedido.CANCELADO,
                EstadoPedido.RECHAZADO
        );

        Page<Pedido> paginaPedidos = repoPedido.findByCliente_IdUsuarioAndEstadoPedidoIn(idCliente, estadosHistorial, pageable);

        return paginaPedidos.map(pedidoMapper::pedidoToPedidoClienteDto);
    }

    private Long getIdUsuarioDesdeAuth0(String idAuth0) {
        Usuario usuario = repoUsuario.findByIdAuth0(idAuth0)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getIdUsuario();
    }
}
