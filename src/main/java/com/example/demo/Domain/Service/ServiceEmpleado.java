package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Usuario.PerfilEmpleadoDto;
import com.example.demo.Application.Mapper.UsuarioMapper;
import com.example.demo.Domain.Entities.Empleado;
import com.example.demo.Domain.Repositories.RepoEmpleado;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class ServiceEmpleado {

    private final RepoEmpleado repoEmpleado;
    private final UsuarioMapper usuarioMapper;

    public ServiceEmpleado(RepoEmpleado repoEmpleado, UsuarioMapper usuarioMapper) {
        this.repoEmpleado = repoEmpleado;
        this.usuarioMapper = usuarioMapper;
    }

    public PerfilEmpleadoDto datos(OidcUser _empleado) {
        Empleado empleado = repoEmpleado.findByEmail(_empleado.getEmail());

        PerfilEmpleadoDto perfilEmpleadoDto = usuarioMapper.empleadoToPerfilEmpleadoDto(empleado);
        return perfilEmpleadoDto;
    }
}
