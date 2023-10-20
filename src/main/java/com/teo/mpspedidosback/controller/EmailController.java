package com.teo.mpspedidosback.controller;


import com.teo.mpspedidosback.entity.EmailEntity;
import com.teo.mpspedidosback.service.api.IEmailService;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.Session;

import static com.teo.mpspedidosback.service.EmailService.configureEmailSession;

@RestController
@RequestMapping("/apiPedidosMps/v1/email")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8082", "http://192.190.42.51:8082", "http://192.190.42.51:8083" })
public class EmailController {



    @Autowired
    private IEmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailEntity emailRequest) {
        try {
            // Crear una sesión de correo electrónico
            Session emailSession = configureEmailSession(emailRequest);

            // Crear el mensaje de correo electrónico utilizando la sesión y los datos de la solicitud
            Message emailMessage = emailService.createEmail(emailSession, emailRequest);

            // Enviar el correo electrónico
            emailService.sendEmail(emailMessage);

            return ResponseEntity.ok("Correo enviado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo: " + e.getMessage());
        }
    }

    }







