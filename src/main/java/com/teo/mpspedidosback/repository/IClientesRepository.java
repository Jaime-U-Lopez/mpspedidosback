package com.teo.mpspedidosback.repository;

import com.teo.mpspedidosback.entity.ClientesEntity;
import com.teo.mpspedidosback.entity.ProductosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IClientesRepository extends JpaRepository<ClientesEntity, Long> {


    Optional<ClientesEntity> findByNit(Long nit);
    List<ClientesEntity> findByNombreContaining(String nombre);

    List<ClientesEntity> findByNombreAndNit(String nombre, Long nit);
}
