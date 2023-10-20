package com.teo.mpspedidosback.dto;


import lombok.Data;


@Data
public class ProductosDtoRequest {

    private Long id;
    private Integer cantidad;
    private Integer valorUnitario;

    public ProductosDtoRequest() {
    }
}
