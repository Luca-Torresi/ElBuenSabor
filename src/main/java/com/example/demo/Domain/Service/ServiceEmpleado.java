package com.example.demo.Domain.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.Application.DTO.Usuario.ActualizarEmpleadoDto;
import com.example.demo.Application.DTO.Usuario.EmpleadoResponseDto;
import com.example.demo.Application.DTO.Usuario.NuevoEmpleadoDto;
import com.example.demo.Application.Mapper.UsuarioMapper;
import com.example.demo.Domain.Entities.Direccion;
import com.example.demo.Domain.Entities.Empleado;
import com.example.demo.Domain.Repositories.RepoDepartamento;
import com.example.demo.Domain.Repositories.RepoDireccion;
import com.example.demo.Domain.Repositories.RepoEmpleado;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceEmpleado {

    private final RepoEmpleado repoEmpleado;
    private final UsuarioMapper usuarioMapper;
    private final RepoDireccion repoDireccion;
    private final RepoDepartamento repoDepartamento;

    public void cargarNuevoEmpleado(NuevoEmpleadoDto nuevoEmpleadoDto) {
        Empleado empleado = usuarioMapper.nuevoEmpleadoDtoToEmpleado(nuevoEmpleadoDto);

        if (empleado.getDireccion() != null) {
            Long idDepartamento = nuevoEmpleadoDto.getDireccion().getIdDepartamento();
            if (idDepartamento != null) {
                var departamento = repoDepartamento.findById(idDepartamento)
                        .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
                empleado.getDireccion().setDepartamento(departamento);
            }

            if (empleado.getDireccion().getIdDireccion() == null) {
                repoDireccion.save(empleado.getDireccion());
            }
        }

        repoEmpleado.save(empleado);
    }

    public void altaBajaEmpleado(Long idEmpleado) {
        Empleado empleado = repoEmpleado.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        empleado.setFechaBaja(empleado.getFechaBaja() != null ? null : LocalDate.now());
        repoEmpleado.save(empleado);
    }

    public void modificarEmpleado(Long idEmpleado, ActualizarEmpleadoDto dto) {
        Empleado empleado = repoEmpleado.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        usuarioMapper.updateEmpleadoFromDto(dto, empleado);

        if (dto.getDireccion() != null) {
            Direccion direccion = usuarioMapper.direccionDtoToDireccion(dto.getDireccion());

            if (dto.getDireccion().getIdDepartamento() != null) {
                var departamento = repoDepartamento.findById(dto.getDireccion().getIdDepartamento())
                        .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
                direccion.setDepartamento(departamento);
            }

            empleado.setDireccion(direccion);

            if (direccion.getIdDireccion() == null) {
                repoDireccion.save(direccion);
            }
        }

        repoEmpleado.save(empleado);
    }

    public List<Empleado> obtenerTodos() {
        return repoEmpleado.findAll();
    }

    public Empleado obtenerPorId(Long id) {
        return repoEmpleado.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }

    public List<EmpleadoResponseDto> obtenerEmpleadosFormateados() {
        return repoEmpleado.findAll()
                .stream()
                .map(usuarioMapper::empleadoToEmpleadoResponseDto)
                .collect(Collectors.toList());
    }

    public EmpleadoResponseDto obtenerEmpleadoFormateadoPorId(Long id) {
        Empleado empleado = repoEmpleado.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        return usuarioMapper.empleadoToEmpleadoResponseDto(empleado);
    }
}
