package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Generic.AltaBajaDto;
import com.example.demo.Application.DTO.Usuario.NuevoEmpleadoDto;
import com.example.demo.Application.Mapper.UsuarioMapper;
import com.example.demo.Domain.Entities.Empleado;
import com.example.demo.Domain.Repositories.RepoEmpleado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ServiceEmpleado {
    private RepoEmpleado repoEmpleado;
    private UsuarioMapper usuarioMapper;

    //Carga un nuevo empleado en el sistema
    public void cargarNuevoEmpleado(NuevoEmpleadoDto nuevoEmpleadoDto){
        Empleado empleado = usuarioMapper.nuevoEmpleadoDtoToEmpleado(nuevoEmpleadoDto);
        repoEmpleado.save(empleado);
    }

    //Dar de alta o baja a un empleado
    public void altaBajaEmpleado(Long idEmpleado){
        Empleado empleado = repoEmpleado.findById(idEmpleado).get();
        empleado.setFechaBaja(
                empleado.getFechaBaja() != null ? null : LocalDate.now()
        );
        repoEmpleado.save(empleado);
    }
}
