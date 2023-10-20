package com.teo.mpspedidosback.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Productos")
@Data
public class ProductosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numerodeparte;
    private String cantidad;
    private String descripcion;
    private String tipoDeNegocio;
    private String marca;
    private String color;
    private String clasificaciontributaria;
    private String moneda;
    private String grupodeimpuestos;
    private String Stock;
    private String preciocompra;
    private String preciominimocop;
    private String preciomaximocop;
    private String preciominimousd;
    private String preciomaximousd;

    public ProductosEntity() {
    }
}
