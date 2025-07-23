package com.example.demo.Domain.Service;

import com.example.demo.Domain.Entities.DetalleFactura;
import com.example.demo.Domain.Entities.Factura;
import com.example.demo.Domain.Repositories.RepoFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceFactura {
    private final RepoFactura repoFactura;
    private final JavaMailSender javaMailSender;

    //Obtiene los datos necesario y genera el PDF correspondiente a la factura
    public byte[] generarFacturaPDF(Long idPedido) {
        Factura factura = repoFactura.findByIdPedido(idPedido);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();

            // Logo (espacio para cargar imagen por URL)
            /*
            Image logo = Image.getInstance("URL_DEL_LOGO"); // <- Reemplazar
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_RIGHT);
            document.add(logo);
            */

            // Nombre del restaurante
            Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph titulo = new Paragraph("El Buen Sabor");
            titulo.setFont(tituloFont);
            titulo.setAlignment(Element.ALIGN_LEFT);
            document.add(titulo);

            document.add(new Paragraph(" ")); // Espacio

            // Info de la factura
            document.add(new Paragraph("Factura N°: " + factura.getNroComprobante()));
            document.add(new Paragraph("Fecha: " + factura.getFechaYHora().toLocalDate()));
            document.add(new Paragraph("Hora: " + factura.getFechaYHora().toLocalTime()));

            // Cliente (desde el pedido)
            String nombreCliente = factura.getPedido().getCliente().getNombre() + " " + factura.getPedido().getCliente().getApellido();
            document.add(new Paragraph("Cliente: " + nombreCliente));
            document.add(new Paragraph("Email: " + factura.getPedido().getCliente().getEmail()));

            document.add(new Paragraph(" ")); // Espacio

            // Tabla de detalles
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new int[]{4, 2, 2, 2});

            tabla.addCell("Artículo");
            tabla.addCell("Cantidad");
            tabla.addCell("Precio Unitario");
            tabla.addCell("Subtotal");

            for (DetalleFactura detalle : factura.getDetalles()) {
                tabla.addCell(detalle.getNombreArticulo());
                tabla.addCell(String.valueOf(detalle.getCantidad()));
                tabla.addCell("$" + detalle.getPrecioUnitario());
                tabla.addCell("$" + detalle.getSubTotal());
            }

            document.add(tabla);
            document.add(new Paragraph(" ")); // Espacio

            // Total y método de pago
            document.add(new Paragraph("Total: $" + factura.getTotal()));
            document.add(new Paragraph("Método de pago: " + factura.getMetodoDePago().toString()));

            document.close();

            return byteArrayOutputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar PDF de factura", e);
        }
    }

    //Llama al método anterior para generar la factura y luego es enviada por mail al cliente
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void enviarFacturaPorMail(Long idPedido) {
        Factura factura = repoFactura.findByIdPedido(idPedido);

        byte[] pdfBytes = generarFacturaPDF(idPedido);

        String emailDestinatario = "email@cliente.com"; // Por defecto

        if (factura.getPedido() != null && factura.getPedido().getCliente() != null) {
            emailDestinatario = factura.getPedido().getCliente().getEmail();
        }

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailDestinatario);
            helper.setSubject("Factura " + factura.getNroComprobante() + " - El Buen Sabor");
            helper.setText("Adjuntamos su factura. ¡Gracias por su compra!");

            helper.addAttachment("factura_" + factura.getNroComprobante() + ".pdf", new ByteArrayResource(pdfBytes));

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el mail con factura", e);
        }
    }
}
