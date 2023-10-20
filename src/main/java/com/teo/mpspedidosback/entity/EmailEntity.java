package com.teo.mpspedidosback.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Email")
@Data
public class EmailEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destinatario;
    private String remitente;
    private String asunto;

    private String cuerpoCorreo;

    public EmailEntity() {
    }

}
