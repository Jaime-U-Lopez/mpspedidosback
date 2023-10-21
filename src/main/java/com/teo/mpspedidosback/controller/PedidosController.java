package com.teo.mpspedidosback.controller;


import com.teo.mpspedidosback.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.teo.mpspedidosback.configuration.Constants;
import com.teo.mpspedidosback.entity.PedidosEntity;
import com.teo.mpspedidosback.entity.ProductosEntity;
import com.teo.mpspedidosback.service.api.IPedidosService;
import jakarta.persistence.PostUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin(origins = "http://localhost:3000") // Reemplaza con la URL de tu aplicación React
@CrossOrigin(origins = {"http://localhost:8082", "http://192.190.42.51:8082", "http://192.190.42.51:8083" }) // Reemplaza con la URL de tu aplicación React
@RequestMapping("/apiPedidosMps/v1/pedidos")
@RequiredArgsConstructor
public class PedidosController {

    @Autowired
    private IPedidosService pedidosService;
    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createPedidos(@Valid @RequestBody PedidoDtoRequest pedidoDtoRequest){
        pedidosService.createPedidos(pedidoDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.CREADO_PEDIDOS_INICIAL));
    }


    @PostMapping("email/")
    public ResponseEntity<Map<String, String>> createCorreoPedidos(@Valid @RequestBody PedidoCamEstadoDtoRequest pedidoCamEstadoDtoRequest){
        pedidosService.enviarCorreo(pedidoCamEstadoDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.CORREO_ENVIADO_CON_EXITO));
    }

    @PostMapping("emailCartera/")
    public ResponseEntity<Map<String, String>> createCorreoCartera(@Valid @RequestBody PedidoEmailCarteraDtoRequest pedidoEmailCarteraDtoRequest){
        pedidosService.enviarCorreoCartera(pedidoEmailCarteraDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.CORREO_ENVIADO_CON_EXITO));
    }


    @PatchMapping("/")
    public  ResponseEntity<Map<String, String>>UpdatePedidosConfirmacion(@Valid @RequestBody PedidoConfirmarDtoRequest pedidoConfirmarDtoRequest){
        pedidosService.updatePedidosConfirmacion(pedidoConfirmarDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.PEDIDOS_UPDATE_CON_EXITO));
    }

    @PatchMapping("addProduct/")
    public  ResponseEntity<Map<String, String>>UpdatePedidosSinConfirmar(@Valid @RequestBody PedidoDtoUpdateRequest pedidoDtoUpdateRequest){
        pedidosService.updatePedidos(pedidoDtoUpdateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.PEDIDOS_UPDATE_CON_EXITO));
    }


    @GetMapping("/")
    public ResponseEntity<List<PedidoAcumuladoDtoResponse>> getAllPedidosAcumulados(){
        return ResponseEntity.ok(pedidosService.getAllPedidosAcumulados());
    }

    @GetMapping("conteo/")
    public ResponseEntity<Integer> getConteoProductos(){
        return ResponseEntity.ok(pedidosService.conteoPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidosEntity> getPedidos(@PathVariable Long id){
        return ResponseEntity.ok(pedidosService.getPedidos(id));
    }
    @GetMapping("orden/{orden}")
    public ResponseEntity<List<PedidoDtoResponse>> getByCodigoInterno(@Valid String orden ){
        return ResponseEntity.ok(pedidosService.findByCodigoInternServ(orden));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProducto(@Valid Long  id) {
        pedidosService.deletePedidos(id);
        return ResponseEntity.ok(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.ENTIDAD_ElIMINADA_MENSAJE));
    }


    @DeleteMapping("codigo/{id}")
    public ResponseEntity<Map<String, String>> deleteByCodigoControl(@Valid String  codigo) {
        pedidosService.deleteByCodigoPedido(codigo);
        return ResponseEntity.ok(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.ENTIDAD_ElIMINADA_MENSAJE));
    }

    @GetMapping("consulta/valor/{valor}")
    public ResponseEntity<List<PedidoAcumuladoDtoResponse>> consultaPorValorAcumulado(@Valid Integer valor ){
        return ResponseEntity.ok(pedidosService.calcularSumaPedidosSuperiorAValor(valor));
    }





}
