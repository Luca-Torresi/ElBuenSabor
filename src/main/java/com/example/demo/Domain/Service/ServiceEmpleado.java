package com.example.demo.Domain.Service;

import com.example.demo.Application.Mapper.UsuarioMapper;
import com.example.demo.Domain.Repositories.RepoEmpleado;
import org.springframework.stereotype.Service;

@Service
public class ServiceEmpleado {

    private final RepoEmpleado repoEmpleado;

    public ServiceEmpleado(RepoEmpleado repoEmpleado) {
        this.repoEmpleado = repoEmpleado;
    }
}
