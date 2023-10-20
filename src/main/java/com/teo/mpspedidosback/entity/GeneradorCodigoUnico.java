package com.teo.mpspedidosback.entity;

public class GeneradorCodigoUnico {
    private int ultimoCodigoGenerado;

    public GeneradorCodigoUnico() {
        // Inicializa el último código generado con 999 para que el siguiente sea 1000.
        this.ultimoCodigoGenerado = 999;
    }

    public int generarNuevoCodigo() {
        // Incrementa el último código generado en 1 para obtener el nuevo código.
        this.ultimoCodigoGenerado++;
        return this.ultimoCodigoGenerado;
    }
}
