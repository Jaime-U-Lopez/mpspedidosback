package com.teo.mpspedidosback.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Table(name = "Pedidos")
@Data
public class PedidosEntity {



    public static  Integer contador=1000;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreComercial;
    private Long dni;
    private Date fechaCreacion;
    private String tipoDocumento;
    private String direccion;
    private String celular;
    private String telefonoFijo;
    private Integer numeroPedido;
    private Double ivaPorProd;
    private String clasificacionTributaria;
    private String estado;
    private String correoElectronico;
    private String codigoInterno;
    private Double valorTotalPedido;
    private Double netoApagar;
    private Double ivaTotalPed;
    //productos
    private String numerodeparte;
    private Integer cantidad;
    private Integer cantidadTotalPed;
    private String descripcion;
    private String tipoDeNegocio;
    private String marca;
    private String color;
    private String observaciones;
    private String moneda;
    private String grupodeimpuestos;
    private String Stock;
    private Integer valorUnitario;

    private  Double valorNetoPorProd;
    private String evento;
    private  Long valorTotalPorPro;
    private String preciominimocop;
    private String preciomaximocop;
    private String preciominimousd;
    private String preciomaximousd;
    private String personaContacto;
private String correoComercial;
    private String formaDePago;
    public PedidosEntity() {
        contador++;
    }

    public PedidosEntity(String nombreComercial, Long dni, Integer valorUnitario,
                         Double ivaPorProd, String estado, String correoElectronico, String codigoInterno,
                         String numerodeparte, Integer cantidad,  String descripcion,
                         String marca, String color, String Stock, String clasificacionTributaria,
                         Integer numeroPedido, String tipoDeNegocio,String  preciominimocop,
                         String preciominimousd, Double valorNetoPorProd ,Long  valorTotalPorPro) {
        this.nombreComercial = nombreComercial;
        this.dni = dni;
        this.valorUnitario=valorUnitario;

        this.ivaPorProd = ivaPorProd;
        this.estado = estado;
        this.correoElectronico = correoElectronico;
        this.codigoInterno = codigoInterno;
        this.numerodeparte = numerodeparte;
        this.cantidad = cantidad;
        this.numeroPedido=numeroPedido;
        this.descripcion = descripcion;
        this.marca = marca;
        this.color = color;
        this.Stock = Stock;
        this.clasificacionTributaria=clasificacionTributaria;
        this.tipoDeNegocio=tipoDeNegocio;
        this.preciominimocop=preciominimocop;
        this.preciominimousd=preciominimousd;
        this.valorNetoPorProd=valorNetoPorProd;
        this.valorTotalPorPro=valorTotalPorPro;




    }


    public PedidosEntity(String nombreComercial, Long dni, Integer valorUnitario,
                         Double ivaPorProd, String estado, String correoElectronico, String codigoInterno,
                         String numerodeparte, Integer cantidad,  String descripcion,
                         String marca, String color, String Stock, String clasificacionTributaria,
                         Integer numeroPedido, String tipoDeNegocio,String  preciominimocop,
                         String preciominimousd, Double valorNetoPorProd ,Long  valorTotalPorPro,
                         String correoComercial

                         ) {
        this.nombreComercial = nombreComercial;
        this.dni = dni;
        this.valorUnitario=valorUnitario;

        this.ivaPorProd = ivaPorProd;
        this.estado = estado;
        this.correoElectronico = correoElectronico;
        this.codigoInterno = codigoInterno;
        this.numerodeparte = numerodeparte;
        this.cantidad = cantidad;
        this.numeroPedido=numeroPedido;
        this.descripcion = descripcion;
        this.marca = marca;
        this.color = color;
        this.Stock = Stock;
        this.clasificacionTributaria=clasificacionTributaria;
        this.tipoDeNegocio=tipoDeNegocio;
        this.preciominimocop=preciominimocop;
        this.preciominimousd=preciominimousd;
        this.valorNetoPorProd=valorNetoPorProd;
        this.valorTotalPorPro=valorTotalPorPro;
        this.correoComercial=correoComercial;



    }



    public String getTipoDeNegocio() {
        return tipoDeNegocio;
    }

    public void setTipoDeNegocio(String tipoDeNegocio) {
        this.tipoDeNegocio = tipoDeNegocio;
    }

    public static Integer getContador() {
        return contador;
    }
}
