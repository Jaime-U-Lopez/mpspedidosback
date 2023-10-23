package com.teo.mpspedidosback.service.api;


import com.teo.mpspedidosback.dto.UsuarioDtoResponse;
import com.teo.mpspedidosback.dto.UsuarioValidacionDtoRequest;
import com.teo.mpspedidosback.entity.UsuariosEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUsuariosService {

    void createUser( UsuariosEntity usuariosEntity );
    void updateUser( UsuariosEntity usuariosEntity );
    UsuariosEntity  getUser(Long id);

    List<UsuariosEntity> getAllUser();
    void deleteUser(Long codigo);


    void cargarUsuariosPorPlano(MultipartFile archivo) throws IOException;;


    UsuarioDtoResponse  getfindByUsuario(String usuario);


    void validacionUser(UsuarioValidacionDtoRequest usuarioValidacionDtoRequest);

}
