package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Usuario.DireccionDto;
import com.example.demo.Application.DTO.Usuario.PerfilUsuarioDto;
import com.example.demo.Application.DTO.Usuario.RegistroClienteDto;
import com.example.demo.Application.Mapper.DireccionMapper;
import com.example.demo.Application.Mapper.UsuarioMapper;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Entities.Departamento;
import com.example.demo.Domain.Entities.Direccion;
import com.example.demo.Domain.Entities.Usuario;
import com.example.demo.Domain.Exceptions.ClienteNoRegistradoException;
import com.example.demo.Domain.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceCliente {
    private final RepoCliente repoCliente;
    private final UsuarioMapper usuarioMapper;
    private final RepoDepartamento repoDepartamento;
    private final DireccionMapper direccionMapper;

    //Evalua si el cliente se encuentra registrado en la base de datos
    public PerfilUsuarioDto verificacionCliente(OidcUser _cliente) {
        String idAuth0 = _cliente.getSubject();
        if(repoCliente.existsByIdAuth0(idAuth0)){
            Optional<Cliente> clienteOpt = repoCliente.findByEmail(_cliente.getEmail());
            PerfilUsuarioDto perfilUsuarioDto = usuarioMapper.clienteToPerfilUsuarioDto(clienteOpt.get());
            return perfilUsuarioDto;
        } else{
            throw new ClienteNoRegistradoException("El cliente no se encuentra registrado");
        }
    }

    //Persiste en la base de datos la información del nuevo cliente
    public PerfilUsuarioDto registro(OidcUser _cliente, RegistroClienteDto registroCLienteDto){
        Optional<Departamento> departamentoOpt = repoDepartamento.findById(registroCLienteDto.getDireccionDto().getIdDepartamento());

        Direccion direccion = direccionMapper.direccionDtoToDireccion(registroCLienteDto.getDireccionDto());
        direccion.setDepartamento(departamentoOpt.get());

        Cliente cliente = usuarioMapper.registroClienteDtoToCliente(registroCLienteDto);
        cliente.setDireccion(direccion);
        cliente.setEmail(_cliente.getEmail());
        cliente.setIdAuth0(_cliente.getSubject());

        Cliente nuevoCliente = repoCliente.save(cliente);

        return usuarioMapper.clienteToPerfilUsuarioDto(nuevoCliente);
    }

    //Actualiza la información de la dirección del cliente
    public void cambiarDireccionCliente(OidcUser _cliente, DireccionDto direccionDto){
        Optional<Cliente> clienteOpt = repoCliente.findByEmail(_cliente.getEmail());
        Optional<Departamento> departamentoOpt = repoDepartamento.findById(direccionDto.getIdDepartamento());

        Direccion direccion = direccionMapper.direccionDtoToDireccion(direccionDto);
        direccion.setDepartamento(departamentoOpt.get());

        clienteOpt.ifPresent(cliente -> {
            cliente.setDireccion(direccion);
        });
        repoCliente.save(clienteOpt.get());
    }
}
