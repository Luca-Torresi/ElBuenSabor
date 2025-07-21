package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Direccion.ArregloDireccionDto;
import com.example.demo.Application.DTO.Direccion.DireccionDto;
import com.example.demo.Application.DTO.Direccion.NuevaDireccionDto;
import com.example.demo.Application.Mapper.DireccionMapper;
import com.example.demo.Domain.Entities.Cliente;
import com.example.demo.Domain.Entities.Departamento;
import com.example.demo.Domain.Entities.Direccion;
import com.example.demo.Domain.Repositories.RepoCliente;
import com.example.demo.Domain.Repositories.RepoDepartamento;
import com.example.demo.Domain.Repositories.RepoDireccion;
import com.example.demo.Domain.Exceptions.ClienteNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceDireccion {
    private final RepoDireccion repoDireccion;
    private final RepoCliente repoCliente;
    private final DireccionMapper direccionMapper;
    private final RepoDepartamento repoDepartamento;

    //Obtenemos las direcciones de un cliente
    public ArregloDireccionDto obtenerDireccionesCliente(String auth0Id){
        Cliente cliente = repoCliente.findByIdAuth0(auth0Id)
                .orElseThrow(() -> new ClienteNoEncontradoException("No se encontró el cliente en la base de datos"));

        List<Direccion> direcciones = repoDireccion.findByClienteIdAndActivoTrue(cliente.getIdUsuario());

        ArregloDireccionDto arregloDireccionDto = new ArregloDireccionDto();
        List<DireccionDto> direccionesDto = new ArrayList<>();

        for(Direccion direccion : direcciones){
            DireccionDto dto = direccionMapper.direccionToDireccionDto(direccion);
            direccionesDto.add(dto);
        }
        arregloDireccionDto.setDirecciones(direccionesDto);
        return arregloDireccionDto;
    }

    //Guarda una nueva dirección de un cliente
    public Direccion nuevaDireccion(String auth0Id, NuevaDireccionDto dto){
        Cliente cliente = repoCliente.findByIdAuth0(auth0Id)
                .orElseThrow(() -> new ClienteNoEncontradoException("No se encontró el cliente en la base de datos"));

        Departamento departamento = repoDepartamento.findById(dto.getIdDepartamento()).get();

        Direccion direccion = direccionMapper.nuevaDireccionDtoToDireccion(dto);
        direccion.setActivo(true);
        direccion.setCliente(cliente);
        direccion.setDepartamento(departamento);

        return repoDireccion.save(direccion);
    }

    //Modifica la dirección de un cliente
    public Direccion modificarDireccion(String auth0Id, Long idDireccion, NuevaDireccionDto dto){
        Cliente cliente = repoCliente.findByIdAuth0(auth0Id)
                .orElseThrow(() -> new ClienteNoEncontradoException("No se encontró el cliente en la base de datos"));

        Departamento departamento = repoDepartamento.findById(dto.getIdDepartamento()).get();

        Direccion direccion = repoDireccion.findById(idDireccion).get();
        direccion.setNombre(dto.getNombre());
        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setPiso(dto.getPiso());
        direccion.setDpto(dto.getDpto());
        direccion.setDepartamento(departamento);

        return repoDireccion.save(direccion);
    }

    //Eliminar la dirección de un cliente
    public void eliminarDireccion(Long idDireccion){
        Direccion direccion = repoDireccion.findById(idDireccion).get();
        direccion.setActivo(false);
        repoDireccion.save(direccion);
    }
}
