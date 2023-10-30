package com.teo.mpspedidosback.dto;


import lombok.Data;

import java.sql.Date;


@Data
public class PedidoAcumuladoConsultaDtoResponse {



        private String dni;
        private String nombreComercial;

        private String estado;
        private Double netoApagar;
        private Integer unidadesAcumuladas;



}








