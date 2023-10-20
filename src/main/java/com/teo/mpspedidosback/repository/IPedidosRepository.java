package com.teo.mpspedidosback.repository;

import com.teo.mpspedidosback.entity.PedidosEntity;
import com.teo.mpspedidosback.entity.ProductosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPedidosRepository extends JpaRepository<PedidosEntity, Long> {

    List<PedidosEntity>  findByCodigoInterno(String codigoInterno);

    @Query("SELECT SUM(pe.valorTotalPedido) FROM PedidosEntity pe WHERE pe.valorTotalPedido > :valorLimite")
    List<PedidosEntity> calcularSumaPedidosSuperiorAValor(@Param("valorLimite") Double valorLimite);



    void deleteByCodigoInterno(String codigoInterno);
}
