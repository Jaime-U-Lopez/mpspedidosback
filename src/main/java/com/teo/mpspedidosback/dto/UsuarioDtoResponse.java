package com.teo.mpspedidosback.dto;


import lombok.Data;


@Data
public class UsuarioDtoResponse {

    private String  usuario;
    private String  nombreUsuario;
    private String  rol;

    public UsuarioDtoResponse() {
    }
}
