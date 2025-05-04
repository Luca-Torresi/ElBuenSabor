package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Usuario.PerfilClienteDto;
import com.example.demo.Application.Mapper.UsuarioMapper;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Repositories.RepoCliente;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class ServiceCliente {

    private final RepoCliente repoCliente;
    private final UsuarioMapper usuarioMapper;

    public ServiceCliente(RepoCliente repoCliente, UsuarioMapper usuarioMapper) {
        this.repoCliente = repoCliente;
        this.usuarioMapper = usuarioMapper;
    }

    public PerfilClienteDto datos(OidcUser _cliente) {
        Cliente cliente = repoCliente.findByEmail(_cliente.getEmail());

        PerfilClienteDto perfilClienteDto = usuarioMapper.clienteToPerfilClienteDto(cliente);
        return perfilClienteDto;
    }
}
