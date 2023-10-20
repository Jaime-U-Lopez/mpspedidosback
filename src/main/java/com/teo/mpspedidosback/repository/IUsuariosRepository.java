package com.teo.mpspedidosback.repository;


import com.teo.mpspedidosback.entity.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUsuariosRepository extends JpaRepository<UsuariosEntity, Long> {


    Optional<UsuariosEntity> findByUsuario(String usuario);
}
