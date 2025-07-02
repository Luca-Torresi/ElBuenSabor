package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.UnidadDeMedida.ArregloUnidadDeMedidaDto;
import com.example.demo.Domain.Entities.UnidadDeMedida;
import com.example.demo.Domain.Service.ServiceUnidadDeMedida;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/unidadDeMedida")
public class ControllerUnidadDeMedida {
    private final ServiceUnidadDeMedida serviceUnidadDeMedida;

    //Devuelve una lista con todas las unidades de medida
    @GetMapping("/lista")
    public ResponseEntity<ArregloUnidadDeMedidaDto> listaUnidadDeMedidas(){
        return ResponseEntity.ok(serviceUnidadDeMedida.listarUnidadesDeMedida());
    }
}
