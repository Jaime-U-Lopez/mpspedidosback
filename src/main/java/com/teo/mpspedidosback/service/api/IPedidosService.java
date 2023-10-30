package com.teo.mpspedidosback.service.api;


import com.teo.mpspedidosback.dto.*;
import com.teo.mpspedidosback.entity.PedidosEntity;
import com.teo.mpspedidosback.entity.ProductosEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


public interface IPedidosService {


    void createPedidos(PedidoDtoRequest pedidoDtoRequest);
    void updatePedidosConfirmacion(PedidoConfirmarDtoRequest pedidoConfirmarDtoRequest);

    void updatePedidos(PedidoDtoUpdateRequest pedidoDtoUpdateRequest);
    PedidosEntity  getPedidos(Long id);
    List<PedidoDtoResponse> findByCodigoInternServ(String codigoInterno);

    List<PedidosEntity> getAllPedidos();
    List<PedidoAcumuladoDtoResponse> getAllPedidosAcumulados();

    void deletePedidos(Long codigo);

    void deleteByCodigoPedido(String codigo);

   void enviarCorreo(PedidoCamEstadoDtoRequest pedidoCamEstadoDtoRequest);
    void enviarCorreoCartera(PedidoEmailCarteraDtoRequest pedidoEmailCarteraDtoRequest);

    Integer conteoPedidos();
    List<PedidoAcumuladoConsultaDtoResponse> calcularSumaPedidosSuperiorAValor(Integer valor);



}
