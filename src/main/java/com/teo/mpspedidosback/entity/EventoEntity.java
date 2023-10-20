package com.teo.mpspedidosback.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Evento")
public class EventoEntity {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreEvento;

}
