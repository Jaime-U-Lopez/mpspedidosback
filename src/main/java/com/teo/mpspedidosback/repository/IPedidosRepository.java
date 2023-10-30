package com.teo.mpspedidosback.repository;

import com.teo.mpspedidosback.entity.PedidosEntity;
import com.teo.mpspedidosback.entity.ProductosEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IPedidosRepository extends JpaRepository<PedidosEntity, Long> {

    List<PedidosEntity>  findByCodigoInterno(String codigoInterno);

    @Query("SELECT SUM(pe.valorTotalPedido) FROM PedidosEntity pe WHERE pe.valorTotalPedido > :valorLimite")
    List<PedidosEntity> calcularSumaPedidosSuperiorAValor(@Param("valorLimite") Double valorLimite);

    boolean existsByNumeroPedido(Integer numeroPedido);

    @Query("SELECT MAX(p.numeroPedido) FROM PedidosEntity p")
    Integer findMaxNumeroPedido();

    void deleteByCodigoInterno(String codigoInterno);


    List<PedidosEntity> findByEstadoAndValorTotalPedidoGreaterThan(String estado, Integer valor);

    List<PedidosEntity> findByEstado( String estado);
  PedidosEntity findByDni( Long dni);


}
