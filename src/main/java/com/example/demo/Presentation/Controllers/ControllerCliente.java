package com.example.demo.Presentation.Controllers;

import com.example.demo.Application.DTO.Usuario.DireccionDto;
import com.example.demo.Application.DTO.Usuario.PerfilUsuarioDto;
import com.example.demo.Application.DTO.Usuario.RegistroClienteDto;
import com.example.demo.Domain.Exceptions.ClienteNoRegistradoException;
import com.example.demo.Domain.Service.ServiceCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cliente")
public class ControllerCliente {

    private final ServiceCliente serviceCliente;

    //Registro de un nuevo cliente
    @PostMapping("/registro")
    public ResponseEntity<PerfilUsuarioDto> registroNuevoCliente(@AuthenticationPrincipal OidcUser _cliente, @RequestBody RegistroClienteDto registroClienteDto){
        PerfilUsuarioDto perfilUsuarioDto = serviceCliente.registro(_cliente, registroClienteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilUsuarioDto);
    }

    //Verifica si el cliente se encuentra registrado
    @GetMapping("/verificacion")
    public ResponseEntity<?> verificacionCliente(@AuthenticationPrincipal OidcUser _cliente) {
        try{
            //Si se encuentra registrado, el método devuelve los datos del perfil
            PerfilUsuarioDto perfilUsuarioDto = serviceCliente.verificacionCliente(_cliente);
            return ResponseEntity.ok(perfilUsuarioDto);
        } catch (ClienteNoRegistradoException e){
            //Si no se encuentra registrado,
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //Actualiza la dirección del cliente en la base de datos con la información recibida
    @PostMapping("/cambiarDireccion")
    public void cambiarDireccion(@AuthenticationPrincipal OidcUser _cliente, @RequestBody DireccionDto direccionDto){
        serviceCliente.cambiarDireccionCliente(_cliente, direccionDto);
    }
}
