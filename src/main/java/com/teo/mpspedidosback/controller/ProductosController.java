package com.teo.mpspedidosback.controller;



import com.teo.mpspedidosback.dto.ProductosDtoResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.teo.mpspedidosback.configuration.Constants;
import com.teo.mpspedidosback.entity.ProductosEntity;
import com.teo.mpspedidosback.service.api.IProductosService;
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
@CrossOrigin(origins = {"http://localhost:8082", "http://192.190.42.51:8082", "http://192.190.42.51:8083" }) // Reemplaza con la URL de tu aplicación React
@RequestMapping("/apiPedidosMps/v1/productos")
@RequiredArgsConstructor
public class ProductosController {

    @Autowired
    private IProductosService productosService;


    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createProducto(@Valid @RequestBody ProductosEntity productosEntity){

        productosService.createProducto(productosEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.ENTIDAD_CREADO_MENSAJE)
        );
    }



    @PostMapping("/cargar")
    public ResponseEntity<Map<String, String>> createProductoPorPlano(@RequestParam("archivo") MultipartFile archivo) throws IOException {

        productosService.cargarProductoPorPlano(archivo);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY,Constants.ENTIDAD_CREADO_MENSAJE)
        );
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductosEntity>> getAllProductos(){
        return ResponseEntity.ok(productosService.getAllProducto());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductosEntity> getProducto(@PathVariable Long id){

        return ResponseEntity.ok(productosService.getProducto(id));

    }

    @GetMapping("/{marca}/{numeroParte}")
    public ResponseEntity<List<ProductosEntity>> getProducto(
            @PathVariable String marca ,
            @PathVariable  String numeroParte ){

        List<ProductosEntity> productos = productosService.findByMarcaAndNumerodeparte(marca, numeroParte);

        if (productos.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(productos);
        }
    }



    @GetMapping("marcas/{marca}")
    public ResponseEntity<List<ProductosEntity>> findbyMarca(@PathVariable String marca){
        return ResponseEntity.ok(productosService.findbyMarcaProducto(marca));
    }

    @GetMapping("filtro/{numeroParte}")
    public ResponseEntity<List<ProductosEntity>> findbyNumerodeparte(@PathVariable String numeroParte){
        return ResponseEntity.ok(productosService.getfindByNumerodeparte(numeroParte));
    }



    @GetMapping("marcas/")
    public ResponseEntity<List<ProductosDtoResponse>> getMarcas(){
        return ResponseEntity.ok(productosService.getMarcaProducto());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProducto(@Valid Long  id  ) {
        productosService.deleteProducto(id);
        return ResponseEntity.ok(Collections.singletonMap(Constants.RESPONSE_MESSAGE_KEY, Constants.ENTIDAD_ElIMINADA_MENSAJE));
    }





}
