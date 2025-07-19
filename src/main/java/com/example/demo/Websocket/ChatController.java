package com.example.demo.Websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")       // Lo que recibe desde el frontend (/app/chat.sendMessage)
    @SendTo("/topic/public")                   // A dónde lo reenvía
    public ChatMessage sendMessage(ChatMessage message) {
        // Si querés agregar timestamp desde el backend:
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        message.setTimestamp(timestamp);
        return message;
    }
}
