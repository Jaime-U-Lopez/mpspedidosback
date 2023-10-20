package com.teo.mpspedidosback.entity;

public class GeneradorCodigoUnico {

    private int contador = 1000;

    public String generarCodigoConsecutivo() {
        String codigo = String.valueOf(contador);
        contador++;
        return codigo;
    }







}
