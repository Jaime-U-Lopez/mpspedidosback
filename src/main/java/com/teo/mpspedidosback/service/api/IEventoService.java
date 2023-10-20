package com.teo.mpspedidosback.service.api;


import com.teo.mpspedidosback.dto.ProductosDtoResponse;
import com.teo.mpspedidosback.entity.EventoEntity;
import com.teo.mpspedidosback.entity.ProductosEntity;
import com.teo.mpspedidosback.entity.UsuariosEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface IEventoService {

    void createEvento(EventoEntity eventoEntity);

    EventoEntity getEvento(Long id);



}
