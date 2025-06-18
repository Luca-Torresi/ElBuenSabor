package com.example.demo.Domain.Service;



import com.example.demo.Application.DTO.Departamento.DepartamentoDto;
import com.example.demo.Domain.Repositories.RepoDepartamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final RepoDepartamento repoDepartamento;

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
