package com.teo.mpspedidosback.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Clientes")
@Data
public class ClientesEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String dirección;
    private String correoElectronico;
    private Double saldoUsado;
    private Double cupoTotal;
    private Double disponible;
    private Long  nit;

    public ClientesEntity() {
    }
}
