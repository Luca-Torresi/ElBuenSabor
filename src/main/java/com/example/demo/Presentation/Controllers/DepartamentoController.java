// src/main/java/com/example/demo/Presentation/Controllers/DepartamentoController.java
package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Departamento.DepartamentoDto;
import com.example.demo.Application.DTO.Departamento.NuevoDepartamentoDto;
import com.example.demo.Domain.Service.DepartamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    @GetMapping("/mendoza")
    public List<DepartamentoDto> obtenerDepartamentosMendoza() {
        return departamentoService.obtenerDepartamentosDeMendoza();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<DepartamentoDto> crearDepartamento(@RequestBody NuevoDepartamentoDto departamentoDto) { // <-- ¡CAMBIO AQUÍ!
        try {
            DepartamentoDto nuevoDepartamento = departamentoService.crearDepartamento(departamentoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDepartamento);
        } catch (IllegalArgumentException e) {
            // Devuelve un mensaje de error útil para el cliente.
            return ResponseEntity.badRequest().body(null); // Considera ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}")
        } catch (RuntimeException e) {
            // Siempre es bueno devolver un mensaje al cliente sobre el error.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Considera ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"" + e.getMessage() + "\"}")
        }
    }
}