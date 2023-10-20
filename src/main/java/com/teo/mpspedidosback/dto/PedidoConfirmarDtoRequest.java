package com.teo.mpspedidosback.dto;


import lombok.Data;

import java.util.List;


@Data
public class PedidoConfirmarDtoRequest {

        private String codigoInterno;
        private PedidosConfiDtoRequest datosUpdate;
        private String estado;
        private String evento;
        private String correoAsesor="";

}








