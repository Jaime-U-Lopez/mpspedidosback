package com.teo.mpspedidosback.service.api;


import com.teo.mpspedidosback.entity.ClientesEntity;
import com.teo.mpspedidosback.entity.ProductosEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface IClientesService {

    void createCliente(ClientesEntity clientesEntity);
    void updateCliente(ClientesEntity clientesEntity);


    void cargarClientesPorPlano(MultipartFile archivo)throws IOException;
    ClientesEntity   getCliente(Long id);
    List<ClientesEntity>   getClientebyNit(Long nit);
    List<ClientesEntity> getAllClientes();
    List<ClientesEntity> findByNombreService(String nombre);
    List<ClientesEntity> findByNombreAndNitService(String nombre, Long nit);
    void deleteCliente(Long codigo);

}


