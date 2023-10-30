package com.teo.mpspedidosback.repository;


import com.teo.mpspedidosback.entity.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IUsuariosRepository extends JpaRepository<UsuariosEntity, Long> {


    Optional<UsuariosEntity> findByUsuario(String usuario);



}
