package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Usuario.ActualizarEmpleadoDto;
import com.example.demo.Application.DTO.Usuario.EmpleadoResponseDto;
import com.example.demo.Application.DTO.Usuario.NuevoEmpleadoDto;
import com.example.demo.Domain.Service.ServiceEmpleado;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize; // Recordá cambiar a hasAuthority('ADMINISTRADOR')
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permite CORS, asegurate de configurarlo para producción
@RequestMapping("/empleado")
public class ControllerEmpleado {

    private final ServiceEmpleado serviceEmpleado;

    // El administrador carga un nuevo empleado en el sistema
    @PreAuthorize("hasAuthority('ADMINISTRADOR')") // Usar hasAuthority()
    @PostMapping("/nuevo")
    public void nuevoEmpleado(@RequestBody NuevoEmpleadoDto nuevoEmpleadoDto) {
        serviceEmpleado.cargarNuevoEmpleado(nuevoEmpleadoDto);
    }

    // Dar de alta o baja lógica a un empleado
    @PreAuthorize("hasAuthority('ADMINISTRADOR')") // Usar hasAuthority()
    @PostMapping("/altaBaja/{idEmpleado}")
    public void altaBajaLogica(@PathVariable Long idEmpleado) {
        serviceEmpleado.altaBajaEmpleado(idEmpleado);
    }

    // Modificar datos de un empleado con DTO extendido
    @PreAuthorize("hasAuthority('ADMINISTRADOR')") // Usar hasAuthority()
    @PutMapping("/{id}")
    public void modificarEmpleado(@PathVariable Long id, @RequestBody ActualizarEmpleadoDto dto) {
        serviceEmpleado.modificarEmpleado(id, dto);
    }

    // Obtener todos los empleados (considera añadir PreAuthorize si no es público)
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'GESTOR_REPORTES')") // Ejemplo
    @GetMapping
    public List<EmpleadoResponseDto> listarEmpleados() {
        return serviceEmpleado.obtenerEmpleadosFormateados();
    }

    // Obtener un empleado por ID (considera añadir PreAuthorize)
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'GESTOR_REPORTES', 'CONSULTOR')") // Ejemplo
    @GetMapping("/{id}")
    public EmpleadoResponseDto obtenerEmpleado(@PathVariable Long id) {
        return serviceEmpleado.obtenerEmpleadoFormateadoPorId(id);
    }
}