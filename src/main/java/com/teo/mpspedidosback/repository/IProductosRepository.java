package com.teo.mpspedidosback.repository;

import com.teo.mpspedidosback.entity.ProductosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IProductosRepository extends JpaRepository<ProductosEntity, Long> {

    List<ProductosEntity> findByMarca(String marca);
    List<ProductosEntity> findByMarcaAndNumerodeparte(String marca,String numerodeparte);


    List<ProductosEntity> findByNumerodeparteContaining(String numerodeparte);
    List<ProductosEntity> findByNumerodeparte(String numerodeparte);
    Optional<ProductosEntity> findFirstByNumerodeparte(String numerodeparte);
}
