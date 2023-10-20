package com.teo.mpspedidosback.dto;


import lombok.Data;

import java.util.List;


@Data
public class UsuarioValidacionDtoRequest {

    private String  usuario;
    private String password;


    public UsuarioValidacionDtoRequest() {
    }
}
