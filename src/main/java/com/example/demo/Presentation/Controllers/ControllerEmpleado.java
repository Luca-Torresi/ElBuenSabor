package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Usuario.ActualizarEmpleadoDto;
import com.example.demo.Application.DTO.Usuario.EmpleadoResponseDto;
import com.example.demo.Application.DTO.Usuario.NuevoEmpleadoDto;
import com.example.demo.Domain.Service.ServiceEmpleado;
import com.example.demo.Domain.Service.ServiceImagen;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Recordá cambiar a hasAuthority('ADMINISTRADOR')
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permite CORS, asegurate de configurarlo para producción
@RequestMapping("/empleado")
public class ControllerEmpleado {

    private final ServiceEmpleado serviceEmpleado;
    private final ServiceImagen serviceImagen;

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
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'CAJERO', 'REPARTIDOR', 'COCINERO')") // Ejemplo
    @GetMapping
    public List<EmpleadoResponseDto> listarEmpleados() {
        return serviceEmpleado.obtenerEmpleadosFormateados();
    }

    // Obtener un empleado por ID (considera añadir PreAuthorize)
    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR', 'CAJERO', 'REPARTIDOR', 'COCINERO')") // Ejemplo
    @GetMapping("/{id}")
    public EmpleadoResponseDto obtenerEmpleado(@PathVariable Long id) {
        return serviceEmpleado.obtenerEmpleadoFormateadoPorId(id);
    }

    /**
     * Endpoint para cargar la imagen de perfil de un empleado existente.
     * Reutiliza el método de subida de imagen de perfil de usuario.
     * Solo para roles con permiso (ej. 'ADMINISTRADOR').
     */
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @PostMapping(value = "/{id}/imagen/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadEmpleadoImage(
            @PathVariable("id") Long id, // Aquí es el ID del empleado (que es un Usuario)
            @RequestParam("file") MultipartFile file) {
        try {
            // ¡CAMBIO CLAVE AQUÍ! Llama al método genérico de perfil de usuario
            return serviceImagen.uploadProfileImage(file, id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "ERROR", "message", e.getMessage()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "ERROR", "message", "Error al subir la imagen del empleado: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para eliminar la imagen de perfil de un empleado existente.
     * Reutiliza el método de eliminación de imagen de perfil de usuario.
     * Solo para roles con permiso (ej. 'ADMINISTRADOR').
     */
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @DeleteMapping("/{id}/imagen/delete")
    public ResponseEntity<String> deleteEmpleadoImage(@PathVariable("id") Long id) { // Aquí es el ID del empleado (que es un Usuario)
        try {
            // ¡CAMBIO CLAVE AQUÍ! Llama al método genérico de perfil de usuario
            return serviceImagen.deleteProfileImage(id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"status\":\"ERROR\", \"message\":\"" + e.getMessage() + "\"}");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"status\":\"ERROR\", \"message\":\"Error al eliminar la imagen del empleado: " + e.getMessage() + "\"}");
        }
    }
}