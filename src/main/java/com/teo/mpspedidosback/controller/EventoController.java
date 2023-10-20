package com.teo.mpspedidosback.controller;


import com.teo.mpspedidosback.configuration.Constants;
import com.teo.mpspedidosback.dto.PedidoCamEstadoDtoRequest;
import com.teo.mpspedidosback.dto.UsuarioValidacionDtoRequest;
import com.teo.mpspedidosback.entity.EventoEntity;
import com.teo.mpspedidosback.entity.UsuariosEntity;
import com.teo.mpspedidosback.service.api.IEventoService;
import com.teo.mpspedidosback.service.api.IUsuariosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiPedidosMps/v1/eventos")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8082", "http://192.190.42.51:8082", "http://192.190.42.51:8083" }) // Reemplaza con la URL de tu aplicación React
public class EventoController {

    @Autowired
    private IEventoService eventoService;





    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createEventos(@Valid @RequestBody EventoEntity eventoEntity){
        eventoService.createEvento(eventoEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.CREADO_PEDIDOS_INICIAL));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoEntity> getEvento(@PathVariable Long id){
        return ResponseEntity.ok(eventoService.getEvento(id));
    }


}
