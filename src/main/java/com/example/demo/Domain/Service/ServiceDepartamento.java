// src/main/java/com/example/demo/Domain/Service/DepartamentoService.java
package com.example.demo.Domain.Service;

import com.example.demo.Application.DTO.Direccion.DepartamentoDto;
import com.example.demo.Domain.Repositories.RepoDepartamento;
import com.example.demo.Domain.Repositories.RepoProvincia;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceDepartamento {

    private final RepoDepartamento repoDepartamento;
    private final RepoProvincia repoProvincia;

    public List<DepartamentoDto> obtenerDepartamentosDeMendoza() {
        return repoDepartamento.findByProvinciaNombreIgnoreCase("Mendoza")
                .stream()
                .map(d -> DepartamentoDto.builder()
                        .id(d.getIdDepartamento())
                        .nombre(d.getNombre())
                        .build())
                .collect(Collectors.toList());
    }
}