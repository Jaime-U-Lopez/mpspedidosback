package com.teo.mpspedidosback.repository;

import com.teo.mpspedidosback.entity.EventoEntity;
import com.teo.mpspedidosback.entity.ProductosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface IEventoRepository extends JpaRepository<EventoEntity, Long> {


}
