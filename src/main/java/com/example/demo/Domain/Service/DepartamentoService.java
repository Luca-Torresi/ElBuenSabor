// src/main/java/com/example/demo/Domain/Service/DepartamentoService.java
package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Departamento.DepartamentoDto;
import com.example.demo.Application.DTO.Departamento.NuevoDepartamentoDto;
import com.example.demo.Domain.Entities.Departamento;
import com.example.demo.Domain.Entities.Provincia;
import com.example.demo.Domain.Repositories.RepoDepartamento;
import com.example.demo.Domain.Repositories.RepoProvincia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional; // Importar Optional
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final RepoDepartamento repoDepartamento;
    private final RepoProvincia repoProvincia;

    public List<DepartamentoDto> obtenerDepartamentosDeMendoza() {
        return repoDepartamento.findByProvinciaNombreIgnoreCase("Mendoza")
                .stream()
                .map(d -> DepartamentoDto.builder()
                        .id(d.getIdDepartamento())
                        .nombre(d.getNombre())
                        .nombreProvincia(d.getProvincia() != null ? d.getProvincia().getNombre() : null)
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional // Marca el método como transaccional
    public DepartamentoDto crearDepartamento(NuevoDepartamentoDto departamentoDto) {
        // Validación básica
        if (departamentoDto.getNombre() == null || departamentoDto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del departamento no puede estar vacío.");
        }

        // --- Lógica para obtener o crear la provincia "Mendoza" ---
        String nombreProvinciaMendoza = "Mendoza";
        Provincia provinciaMendoza = repoProvincia.findByNombreIgnoreCase(nombreProvinciaMendoza)
                .orElseGet(() -> {
                    // Si no encuentra Mendoza, la crea
                    Provincia nuevaProvincia = Provincia.builder()
                            .nombre(nombreProvinciaMendoza)
                            .build();
                    System.out.println("Creando automáticamente la provincia: " + nombreProvinciaMendoza); // Log para saber que la creó
                    return repoProvincia.save(nuevaProvincia);
                });
        // ---------------------------------------------------------

        Departamento departamento = Departamento.builder()
                .nombre(departamentoDto.getNombre())
                .provincia(provinciaMendoza) // Asigna la provincia (existente o recién creada)
                .build();

        Departamento savedDepartamento = repoDepartamento.save(departamento);

        return DepartamentoDto.builder()
                .id(savedDepartamento.getIdDepartamento())
                .nombre(savedDepartamento.getNombre())
                .nombreProvincia(savedDepartamento.getProvincia() != null ? savedDepartamento.getProvincia().getNombre() : null)
                .build();
    }
}