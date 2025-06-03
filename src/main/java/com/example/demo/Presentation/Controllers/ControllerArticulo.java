package com.example.demo.Presentation.Controllers;

import com.example.demo.Domain.Service.ServiceArticulo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articulo")
public class ControllerArticulo {
    private final ServiceArticulo serviceArticulo;
}
