package com.teo.mpspedidosback.dto;


import lombok.Data;

import java.sql.Date;


@Data
public class PedidosConfiDtoRequest {


        private String celular;
        private String correoElectronico;
        private String direccion;
        private String estado;
        private String formaPago;
        private Double ivaTotalPed;
        private Double netoApagar;
        private String nombreComercial;
        private String observaciones;
        private String personaContacto;
        private String telefonoFijo;
        private Double valorTotalPedido;
        private Date fechaCreacion;






}








