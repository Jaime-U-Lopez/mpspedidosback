package com.teo.mpspedidosback.dto;


import lombok.Data;

import java.util.List;


@Data
public class PedidoEmailCarteraDtoRequest {

    private String correo;

    private String estado;
    private String codigoInterno;


    public PedidoEmailCarteraDtoRequest() {
    }
}
