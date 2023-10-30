package com.teo.mpspedidosback.dto;


import lombok.Data;


@Data
public class PedidoDtoResponse {


        private Long id;
        private Long dni;
        private String nombreComercial;
        private String tipoDocumento;
        private Double valorTotal;
        private Double totalIva;
        private Double netoApagar;
        private String formaDePago;
        private String estado;

        private String personaContacto;
        private String direccion;
        private String celular;
        private String telefonoFijo;
        private String correoElectronico;
        private String codigoInterno;
        //productos
        private String numerodeparte;
        private String nombreArticulo;
        private String marca;

        private Integer cantidad;
        private Integer valorUnitario;
        private Integer numeroPedido;
        private String descripcion;
        private String tipoDeNegocio;
        private Double iva;
        private String Stock;

        private String preciominimocop;

        private String preciominimousd;
        private  Double valorNetoPorProd;
        private  Long valorTotalPorPro;
        private String observaciones;

}








