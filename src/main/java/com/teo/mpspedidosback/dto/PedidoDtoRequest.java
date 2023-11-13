package com.teo.mpspedidosback.dto;


import lombok.Data;

import java.util.List;


@Data
public class PedidoDtoRequest {

    private Long idCliente;
    private List<ProductosDtoRequest> listaProductos;
    private String estado;
    private String codigoInterno;
private String correoAsesor;

    public PedidoDtoRequest() {
    }
}
