package com.teo.mpspedidosback.service.api;


import com.teo.mpspedidosback.dto.ProductosDtoResponse;
import com.teo.mpspedidosback.entity.ProductosEntity;
import com.teo.mpspedidosback.entity.UsuariosEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface IProductosService {

    void createProducto(ProductosEntity productosEntity);

    void    cargarProductoPorPlano(MultipartFile archivo) throws IOException;

    ProductosEntity  getProducto(Long id);
    List<ProductosEntity> getAllProducto();
    List<ProductosEntity> getfindByNumerodeparte(String numeroParte);
    List<ProductosEntity> findByMarcaAndNumerodeparte(String marca, String numeroParte);
    List<ProductosDtoResponse> getMarcaProducto();
    List<ProductosEntity> findbyMarcaProducto(String marca);
    void deleteProducto(Long codigo);
    void updateProducto(ProductosEntity productosEntity);





}
