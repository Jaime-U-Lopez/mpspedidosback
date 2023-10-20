package com.teo.mpspedidosback.dto;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
public class ProductosDtoResponse {

    private String marca;

    public ProductosDtoResponse() {
    }
}
