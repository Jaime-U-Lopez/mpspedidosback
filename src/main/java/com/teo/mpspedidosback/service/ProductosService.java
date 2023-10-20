package com.teo.mpspedidosback.service;


import com.teo.mpspedidosback.dto.ProductosDtoResponse;
import com.teo.mpspedidosback.entity.ProductosEntity;
import com.teo.mpspedidosback.exception.ExceptionGeneral;
import com.teo.mpspedidosback.repository.IProductosRepository;
import com.teo.mpspedidosback.service.api.IProductosService;
import com.teo.mpspedidosback.util.ContadorRegistros;
import com.teo.mpspedidosback.util.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductosService implements IProductosService {

    public static int registrosExitosos = 0;
    public static int registrosFallidos = 0;
    public static List<String> errores = new ArrayList<>();
    @Autowired
    private IProductosRepository productosRepository;

    @Override
    public void createProducto(ProductosEntity productosEntity) {
        productosRepository.save(productosEntity);
    }

    @Override
    public void cargarProductoPorPlano(MultipartFile archivo) throws IOException {

        List<ProductosEntity> registrosTemporales = new ArrayList<>();
        if(registrosTemporales.size()>0) {
            registrosTemporales.clear();
        }
        String nombreArchivo = archivo.getOriginalFilename();
        String[] columnasEsperadas = {

                "numerodeparte",

                "descripcion",
                "tipoDeNegocio",
                "marca",
                "color",
                "clasificaciontributaria",
                "moneda",
                "stock",

                "preciominimocop",
                "preciominimousd",

        };
        //para respuesta en error:
        String columnasEsperadasStr = String.join(", ", columnasEsperadas);
        Workbook workbook = null;
        try (InputStream inputStream = archivo.getInputStream()) {
            if (nombreArchivo != null) {
                if (nombreArchivo.endsWith(".xlsx")) {
                    workbook = new XSSFWorkbook(inputStream); // Para archivos .xlsx
                } else if (nombreArchivo.endsWith(".xls")) {
                    // O, si estás usando un archivo .xls:
                    workbook = new HSSFWorkbook(inputStream);
                }
            } else {
                throw new IllegalArgumentException("Tipo de archivo Excel no compatible");
            }

            Sheet sheet = workbook.getSheetAt(0); // Suponiendo que los datos están en la primera hoja
            Row headerRow = sheet.getRow(0);
            if (headerRow != null && headerRow.getPhysicalNumberOfCells() > 0) {
                // Lista para almacenar los nombres de las columnas en el archivo
                List<String> columnasEnArchivo = new ArrayList<>();

                // Itera a través de las celdas de la fila de encabezados
                for (Cell cell : headerRow) {
                    columnasEnArchivo.add(cell.getStringCellValue());
                }
                if (Arrays.equals(columnasEsperadas, columnasEnArchivo.toArray(new String[0]))) {


                    int rowIndex=0;
                    for (rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {

                        try {
                            Row row = sheet.getRow(rowIndex);
                            if (row == null) {
                                continue; // Salta la fila si es nula
                            }
                            if (row.getRowNum() == 0) {

                                continue; // Salta la primera fila si es un encabezado
                            }
                            Cell cell0 = row.getCell(0);
                            Cell cell1 = row.getCell(1);

                            if (cell0 == null || cell1 == null) {
                                // Puedes manejar el caso en el que una de las celdas sea nula, por ejemplo, lanzar una excepción o registrar un mensaje de error.
                                // Aquí, simplemente continuamos con la siguiente fila.
                                registrosFallidos++;
                                continue;
                            }



                            ProductosEntity producto = new ProductosEntity();


                            String numeroParte=row.getCell(0).getStringCellValue();

                         Optional<ProductosEntity> productosEntities= productosRepository.findFirstByNumerodeparte(numeroParte);

                            if(!productosEntities.isPresent()){

                                producto.setNumerodeparte(numeroParte);

                                int maxLength = 255; // Longitud máxima permitida para la descripción
                                String descripcion = row.getCell(1).getStringCellValue(); // Suponiendo que la descripción está en la columna 3

                                if (descripcion.length() > maxLength) {
                                    descripcion = descripcion.substring(0, maxLength); // Acorta el texto si supera la longitud máxima
                                }

                                producto.setDescripcion(descripcion);
                                producto.setTipoDeNegocio(row.getCell(2).getStringCellValue());
                                producto.setMarca(row.getCell(3).getStringCellValue());
                                producto.setColor(row.getCell(4).getStringCellValue());
                                producto.setClasificaciontributaria(row.getCell(5).getStringCellValue());
                                producto.setMoneda(row.getCell(6).getStringCellValue());

                                producto.setStock(row.getCell(7).getStringCellValue());

                                producto.setPreciominimocop(row.getCell(8).getStringCellValue());

                                producto.setPreciominimousd(row.getCell(9).getStringCellValue());


                                registrosTemporales.add(producto);

                            }else{
                                errores.add("Error Numero de parte ya existe fila :" +rowIndex );
                                registrosFallidos++;
                            }


                        }catch (Exception e){

                            errores.add("Error al procesar la fila :" +rowIndex  + ": " + e.getMessage());
                            registrosFallidos++;

                        }
                        }


                        try {
                            productosRepository.saveAll(registrosTemporales);
                            registrosExitosos+=registrosTemporales.size();
                        } catch (Exception e) {
                            // Manejar excepciones, por ejemplo, loguear el error
                            e.printStackTrace();
                            registrosFallidos++;
                            errores.add("Error al procesar la fila " + (registrosFallidos + registrosExitosos) + ": " + e.getMessage());
                        }



                } else {
                    // Los encabezados no coinciden, muestra un mensaje de error
                    throw new IllegalArgumentException("La estructura de las columnas no corresponde. Se esperaba: " + columnasEsperadasStr + " y el archivo tiene: " + String.join(", ", columnasEnArchivo));

                }}

            }catch(IOException e){
                // Manejar errores de lectura del archivo
                e.printStackTrace();
                throw new RuntimeException("Error de lectura del archivo valida los campos ingresados", e);
            }

    }


    @Override
    public ProductosEntity getProducto(Long id) {
        Optional<ProductosEntity>  productosOptional= productosRepository.findById(id);
        return productosOptional.get();
    }

    @Override
    public List<ProductosEntity> getAllProducto() {
        return productosRepository.findAll();
    }

    @Override
    public List<ProductosEntity> getfindByNumerodeparte(String numeroParte) {
        return productosRepository.findByNumerodeparte(numeroParte);
    }

    @Override
    public List<ProductosEntity> findByMarcaAndNumerodeparte( String marca, String numeroParte) {
        return productosRepository.findByMarcaAndNumerodeparte(marca,numeroParte);

    }


    @Override
    public List<ProductosDtoResponse> getMarcaProducto() {

        List<ProductosEntity> productosEntities = productosRepository.findAll();


        Set<String> marcasYaAgregadas = new HashSet<>();
        List<ProductosDtoResponse> productosDtoResponses = productosEntities.stream()
                .filter(entity -> !marcasYaAgregadas.contains(entity.getMarca()))
                .map(entity -> {
                    ProductosDtoResponse dto = new ProductosDtoResponse();
                    dto.setMarca(entity.getMarca());
                    marcasYaAgregadas.add(entity.getMarca());
                    return dto;
                })
                .sorted(Comparator.comparing(ProductosDtoResponse::getMarca))
                .collect(Collectors.toList());


        return productosDtoResponses;
    }




    @Override
    public List<ProductosEntity> findbyMarcaProducto(String marca) {
    return  productosRepository.findByMarca(marca);
    }

    @Override
    public void deleteProducto(Long codigo) {
        productosRepository.deleteById(codigo);
    }

    @Override
    public void updateProducto(ProductosEntity productosEntity) {
        productosRepository.save(productosEntity);
    }




}
