package com.teo.mpspedidosback.controller;


import com.teo.mpspedidosback.service.ClientesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.teo.mpspedidosback.configuration.Constants;
import com.teo.mpspedidosback.entity.ClientesEntity;
import com.teo.mpspedidosback.service.api.IClientesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/apiPedidosMps/v1/clientes/")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:8082", "http://192.190.42.51:8082", "http://192.190.42.51:8083" }) // Reemplaza con la URL de tu aplicación React
public class ClientesController {
    @Autowired
    private final IClientesService clientesService;
    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createCliente(@Valid @RequestBody ClientesEntity clientesEntity){
        clientesService.createCliente(clientesEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.ENTIDAD_CREADO_MENSAJE)
        );
    }


    @CrossOrigin(origins = {"http://localhost:8082","http://192.190.42.51:8082"}, allowCredentials = "true")
    @PostMapping("/cargar")
    public ResponseEntity<Map<String, String>> createClientesPorPlano(@RequestParam("archivo") MultipartFile archivo)throws IOException{
        clientesService.cargarClientesPorPlano(archivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, ClientesService.registrosExitosos+  " Errores Clientes : "+ ClientesService.registrosFallidos+
                        " Detalle :  Fallidos sin crear : " +ClientesService.registrosFallidos +
                        " Errores generados : "+ ClientesService.errores)
        );
    }

    @GetMapping("all/")
    public ResponseEntity<List<ClientesEntity>> getAllClientesService(){
        return ResponseEntity.ok(clientesService.getAllClientes());
    }
    @GetMapping("nombre/")
    public ResponseEntity<List<ClientesEntity>> getAllClientesByNombre(@Valid  String nombre){
        return ResponseEntity.ok(clientesService.findByNombreService(nombre));
    }
    @GetMapping("nombreynit/")
    public ResponseEntity<List<ClientesEntity>> getAllClientesByNombreAndNit(@Valid  String nombre, Long nit){
        return ResponseEntity.ok(clientesService.findByNombreAndNitService(nombre,nit));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClientesEntity> getClientes(@PathVariable Long id){
        return ResponseEntity.ok(clientesService.getCliente(id));
    }


    @GetMapping("nit/")
       public ResponseEntity<List<ClientesEntity> > getClientesByNit(@Valid  @RequestParam Long nit){
        return ResponseEntity.ok(clientesService.getClientebyNit(nit));

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteClientesService(@Valid Long  id  ) {
        clientesService.deleteCliente(id);
        return ResponseEntity.ok(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.ENTIDAD_ElIMINADA_MENSAJE));
    }

}
