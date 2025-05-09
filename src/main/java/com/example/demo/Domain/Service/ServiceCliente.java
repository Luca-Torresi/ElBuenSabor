package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Usuario.PerfilUsuarioDto;
import com.example.demo.Application.DTO.Usuario.RegistroClienteDto;
import com.example.demo.Application.Mapper.UsuarioMapper;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Exceptions.ClienteNoRegistradoException;
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

    //Evalua si el cliente se encuentra registrado en la base de datos
    public PerfilUsuarioDto verificacionCliente(OidcUser _cliente) {
        String idAuth0 = _cliente.getSubject();
        if(repoCliente.existsByIdAuth0(idAuth0)){
            Cliente cliente = repoCliente.findByEmail(_cliente.getEmail());
            PerfilUsuarioDto perfilUsuarioDto = usuarioMapper.clienteToPerfilUsuarioDto(cliente);
            return perfilUsuarioDto;
        } else{
            throw new ClienteNoRegistradoException("El cliente no se encuentra registrado");
        }
    }

    //Persiste en la base de datos la información del nuevo cliente
    public PerfilUsuarioDto registro(OidcUser _cliente, RegistroClienteDto registroCLienteDto){
        Cliente cliente = Cliente.builder()
                .idAuth0(_cliente.getSubject())
                .email(_cliente.getEmail())
                .nombre(registroCLienteDto.getNombre())
                .apellido(registroCLienteDto.getApellido())
                .telefono(registroCLienteDto.getTelefono())
                .urlImagen(registroCLienteDto.getUrlImagen())
                .direccion(registroCLienteDto.getDireccion())
                .build();
        Cliente nuevoCliente = repoCliente.save(cliente);

        return usuarioMapper.clienteToPerfilUsuarioDto(nuevoCliente);
    }
}
