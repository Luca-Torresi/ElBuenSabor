package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Usuario.ActualizarEmpleadoDto;
import com.example.demo.Application.DTO.Usuario.EmpleadoResponseDto;
import com.example.demo.Application.DTO.Usuario.NuevoEmpleadoDto;
import com.example.demo.Domain.Service.ServiceEmpleado;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/empleado")
public class ControllerEmpleado {

    private final ServiceEmpleado serviceEmpleado;

    // El administrador carga un nuevo empleado en el sistema
    @PostMapping("/nuevo")
    public void nuevoEmpleado(@RequestBody NuevoEmpleadoDto nuevoEmpleadoDto) {
        serviceEmpleado.cargarNuevoEmpleado(nuevoEmpleadoDto);
    }

    // Dar de alta o baja lógica a un empleado
    @PostMapping("/altaBaja/{idEmpleado}")
    public void altaBajaLogica(@PathVariable Long idEmpleado) {
        serviceEmpleado.altaBajaEmpleado(idEmpleado);
    }

    // 🔄 Modificar datos de un empleado con DTO extendido
    @PutMapping("/{id}")
    public void modificarEmpleado(@PathVariable Long id, @RequestBody ActualizarEmpleadoDto dto) {
        serviceEmpleado.modificarEmpleado(id, dto);
    }

    // Obtener todos los empleados
    @GetMapping
    public List<EmpleadoResponseDto> listarEmpleados() {
        return serviceEmpleado.obtenerEmpleadosFormateados();
    }

    @GetMapping("/{id}")
    public EmpleadoResponseDto obtenerEmpleado(@PathVariable Long id) {
        return serviceEmpleado.obtenerEmpleadoFormateadoPorId(id);
    }
}
