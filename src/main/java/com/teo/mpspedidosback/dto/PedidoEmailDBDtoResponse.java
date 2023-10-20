package com.teo.mpspedidosback.dto;


import lombok.Data;


@Data
public class PedidoEmailDBDtoResponse {



        private Long dni;
        private String nombreComercial;

        private Double valorTotal;
        private Double totalIva;
        private Double netoApagar;
        private String formaDePago;

        private String personaContacto;
        private String direccion;
        private String celular;
        private String telefonoFijo;
        private String correoElectronico;





}








