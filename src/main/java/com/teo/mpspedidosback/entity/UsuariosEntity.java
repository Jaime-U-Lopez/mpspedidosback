package com.teo.mpspedidosback.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Usuarios")
@Data
public class UsuariosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usuario;
    private String password;
    private String rol;
    private String nombreUsuario;


    public UsuariosEntity() {
    }
}
