package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Usuario.ActualizarEmpleadoDto;
import com.example.demo.Application.DTO.Usuario.EmpleadoResponseDto;
import com.example.demo.Application.DTO.Usuario.NuevoEmpleadoDto;
import com.example.demo.Domain.Service.ServiceEmpleado;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> nuevoEmpleado(@RequestBody NuevoEmpleadoDto nuevoEmpleadoDto) {
        serviceEmpleado.cargarNuevoEmpleado(nuevoEmpleadoDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Dar de alta o baja lógica a un empleado
    @PreAuthorize("hasAuthority('ADMINISTRADOR')") // Usar hasAuthority()
    @PostMapping("/altaBaja/{idEmpleado}")
    public ResponseEntity<Void> altaBajaLogica(@PathVariable Long idEmpleado) throws Exception {
        serviceEmpleado.altaBajaEmpleado(idEmpleado);
        return ResponseEntity.ok().build();
    }

    // Modificar datos de un empleado con DTO extendido
    @PreAuthorize("hasAuthority('ADMINISTRADOR')") // Usar hasAuthority()
    @PutMapping("/{id}")
    public ResponseEntity<Void> modificarEmpleado(@PathVariable Long id, @RequestBody ActualizarEmpleadoDto dto) {
        serviceEmpleado.modificarEmpleado(id, dto);
        return ResponseEntity.ok().build();
    }

    // Obtener todos los empleados (considera añadir PreAuthorize si no es público)
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')") // Ejemplo
    @GetMapping
    public List<EmpleadoResponseDto> listarEmpleados() {
        return serviceEmpleado.obtenerEmpleadosFormateados();
    }

    // Obtener un empleado por ID (considera añadir PreAuthorize)
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')") // Ejemplo
    @GetMapping("/{id}")
    public EmpleadoResponseDto obtenerEmpleado(@PathVariable Long id) {
        return serviceEmpleado.obtenerEmpleadoFormateadoPorId(id);
    }
}