package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Promocion.PromocionDto;
import com.example.demo.Domain.Service.ServicePromocion;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/promocion")
public class ControllerPromocion {
    private final ServicePromocion servicePromocion;

    //Recibe los datos necesarios para la creación de una nueva promoción
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/nueva")
    public void crearNuevaPromocion(@RequestBody PromocionDto promocionDto) {
        servicePromocion.nuevaPromocion(promocionDto);
    }
}
