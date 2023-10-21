package com.teo.mpspedidosback.service;


import com.teo.mpspedidosback.entity.EmailEntity;
import com.teo.mpspedidosback.repository.IEmailRepository;
import com.teo.mpspedidosback.service.api.IEmailService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;


@Data
@Service
public class EmailService implements IEmailService {

    @Autowired
    private IEmailRepository emailRepository;
    @Value("${username}")
    static private String username;

    @Value("${password}")
    static private String password;

    public static Session configureEmailSession(EmailEntity emailEntity) {


        String username = "MPSMatch@mps.com.co";
        String password= "Sistemas-8245";

/*
 String username = "freelancerjulopez@gmail.com";
        String password= "jzed gwlu gbds pxiv";
 */

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587"); // Usar el puerto 587 para TLS (StartTLS)
       // props.put("mail.smtp.ssl.enable", "true");


// Si deseas utilizar SSL, descomenta estas líneas y establece el puerto a 465
// props.put("mail.smtp.ssl.enable", "true");
// props.put("mail.smtp.port", "465");


       /*
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        */
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });

        return session;
    }


    @Override
    public void sendEmail( Message  message) {

        try {
            Transport.send(message);
            System.out.println("Correo Electrónico Enviado Exitosamente");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }

    @Override
    public Message createEmail(Session session, EmailEntity emailEntity ) {
        String username = "MPSMatch@mps.com.co";
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailEntity.getDestinatario()));
            message.setSubject(emailEntity.getAsunto());

            // Configurar el contenido del correo como HTML
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(emailEntity.getCuerpoCorreo(), "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);

            message.setContent(multipart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;


    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
