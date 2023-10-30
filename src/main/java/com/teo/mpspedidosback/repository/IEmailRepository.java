package com.teo.mpspedidosback.repository;

import com.teo.mpspedidosback.entity.ClientesEntity;
import com.teo.mpspedidosback.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmailRepository extends JpaRepository<EmailEntity, Long> {



}
