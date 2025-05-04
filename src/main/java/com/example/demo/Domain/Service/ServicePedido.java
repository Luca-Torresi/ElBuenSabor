package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Pedido.PedidoDto;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Repositories.RepoCliente;
import com.example.demo.Domain.Repositories.RepoPedido;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class ServicePedido {

    private final RepoPedido repoPedido;
    private final RepoCliente repoCliente;

    public ServicePedido(RepoPedido repoPedido, RepoCliente repoCliente) {
        this.repoPedido = repoPedido;
        this.repoCliente = repoCliente;
    }

    public void generarNuevoPedido(OidcUser _cliente, PedidoDto pedidoDto) {
        String email = _cliente.getEmail();
        Cliente cliente = repoCliente.findByEmail(email);
    }
}
