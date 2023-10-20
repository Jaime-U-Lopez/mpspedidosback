package com.teo.mpspedidosback.dto;


import lombok.Data;

import java.sql.Date;


@Data
public class PedidoAcumuladoDtoResponse {


        private Long id;
        private Long dni;
        private String nombreComercial;
        private String codigoInterno;
        private String formaDePago;
        private String Stock;
        private Integer cantidad;
        private Integer numeroPedido;
        private Double valorTotal;
        private Double totalIva;
        private Double netoApagar;
        private String estado;
        private Date fechaCreación;






}








