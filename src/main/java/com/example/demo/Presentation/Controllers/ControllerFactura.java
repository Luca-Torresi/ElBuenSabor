package com.example.demo.Presentation.Controllers;

import com.example.demo.Domain.Service.ServiceFactura;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

@RestController
@RequiredArgsConstructor
@RequestMapping("/factura")
public class ControllerFactura {
    private final ServiceFactura serviceFactura;

    //Método para generar el PDF correspondiente y descargar la factura
    @GetMapping("/descargar/{idPedido}")
    public ResponseEntity<byte[]> descargarFactura(@PathVariable Long idPedido) {
        byte[] pdfBytes = serviceFactura.generarFacturaPDF(idPedido);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("factura.pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    //Método para generar el PDF de la factura y enviarla por email al cliente
    @PostMapping("/enviarPorEmail/{idPedido}")
    public void enviarFacturaPorEmail(@PathVariable Long idPedido) {
        serviceFactura.enviarFacturaPorMail(idPedido);
    }
}
