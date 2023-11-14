package com.teo.mpspedidosback.entity;

public class PedidosAcumulados {

    private String dni;
    private double totalCompra;
    private int unidades;
    private String nombreCliente;
    private Integer numeroPedido;

    public Integer getNumeroPedido() {
        return numeroPedido;
    }

    public PedidosAcumulados(String dni, double totalCompra, int unidades, String nombreCliente, Integer numeroPedido) {
        this.dni = dni;
        this.totalCompra = totalCompra;
        this.unidades = unidades;
        this.numeroPedido =numeroPedido;
        this.nombreCliente = nombreCliente;
    }

    public String getDni() {
        return dni;
    }

    public double getTotalCompra() {
        return totalCompra;
    }


    public int getUnidades() {
        return unidades;
    }
    public String getNombreCliente() {
        return nombreCliente;
    }


}
