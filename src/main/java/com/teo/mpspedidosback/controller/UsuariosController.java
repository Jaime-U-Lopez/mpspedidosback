package com.teo.mpspedidosback.controller;


import com.teo.mpspedidosback.configuration.Constants;
import com.teo.mpspedidosback.dto.PedidoCamEstadoDtoRequest;
import com.teo.mpspedidosback.dto.UsuarioDtoResponse;
import com.teo.mpspedidosback.dto.UsuarioValidacionDtoRequest;
import com.teo.mpspedidosback.entity.ProductosEntity;
import com.teo.mpspedidosback.entity.UsuariosEntity;
import com.teo.mpspedidosback.service.api.IProductosService;
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
@RequestMapping("/apiPedidosMps/v1/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8082", "http://192.190.42.51:8082", "http://192.190.42.51:8083" }) // Reemplaza con la URL de tu aplicación React
public class UsuariosController {

    @Autowired
    private IUsuariosService usuariosService;

    @CrossOrigin(origins = "http://localhost:8082") // Reemplaza con la URL de tu aplicación React
    @PostMapping("/cargar")
    public ResponseEntity<Map<String, String>> createProductoPorPlano(@RequestParam("archivo") MultipartFile archivo) throws IOException {

        usuariosService.cargarUsuariosPorPlano(archivo);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.USUARIO_CREADOS_MENSAJE)
        );
    }



    @PostMapping("/validarUser")
    public ResponseEntity<Map<String, String>> validarUser(@Valid @RequestBody UsuarioValidacionDtoRequest usuarioValidacionDtoRequest) throws IOException {

        usuariosService.validacionUser(usuarioValidacionDtoRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.USUARIO_CREADOS_MENSAJE)
        );
    }


    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createUsuario(@Valid @RequestBody UsuariosEntity usuariosEntity) throws IOException {

        usuariosService.createUser(usuariosEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.USUARIO_CREADOS_MENSAJE)
        );
    }



    @GetMapping("/")
    public ResponseEntity<List<UsuariosEntity>> getAllUsuarios(){
        return ResponseEntity.ok(usuariosService.getAllUser());
    }


    @GetMapping("/{id}")
    public ResponseEntity<UsuariosEntity> getUsuario(@PathVariable Long id){

        return ResponseEntity.ok(usuariosService.getUser(id));

    }

    @GetMapping("usuario/{usuario}")
    public ResponseEntity<UsuarioDtoResponse> getUsuarioByUsuario(@PathVariable String usuario){

        return ResponseEntity.ok(usuariosService.getfindByUsuario(usuario));

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUsuario(@Valid Long  id  ) {
        usuariosService.deleteUser(id);
        return ResponseEntity.ok(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.ENTIDAD_ElIMINADA_MENSAJE));
    }




}
