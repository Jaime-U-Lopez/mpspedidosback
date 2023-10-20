package com.teo.mpspedidosback.service;


import com.teo.mpspedidosback.entity.ClientesEntity;
import com.teo.mpspedidosback.entity.ProductosEntity;
import com.teo.mpspedidosback.exception.ExceptionGeneral;
import com.teo.mpspedidosback.repository.IClientesRepository;
import com.teo.mpspedidosback.service.api.IClientesService;
import com.teo.mpspedidosback.util.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.teo.mpspedidosback.util.ExcelUtils.getLongValue;
import static java.lang.Double.parseDouble;

@Service
public class ClientesService  implements IClientesService {

    public static int registrosExitosos = 0;
    public static int registrosFallidos = 0;
    public static List<String> errores = new ArrayList<>();
    @Autowired
    private IClientesRepository clientesRepository;

    @Override
    public void createCliente(ClientesEntity clientesEntity) {
        clientesRepository.save(clientesEntity);
    }

    @Override
    public void updateCliente(ClientesEntity clientesEntity) {
        clientesRepository.save(clientesEntity);
    }


    @Override
    public ClientesEntity getCliente(Long id) {
         Optional<ClientesEntity> clientesEntityOptional = clientesRepository.findById(id);
        if(!clientesEntityOptional.isPresent()){
              throw new ExceptionGeneral("El Cliente con ese ide no existe");
        }

        return clientesEntityOptional.get();
    }

    @Override
    public List<ClientesEntity>   getClientebyNit(Long nit) {
        Optional<ClientesEntity> clientesEntityOptional = clientesRepository.findByNit(nit);
        if(!clientesEntityOptional.isPresent()){
            throw new ExceptionGeneral("El Cliente con ese ide no existe");
        }

        List<ClientesEntity> clientesEntityList=new ArrayList<>();
        clientesEntityList.add(clientesEntityOptional.get());

        return clientesEntityList;
    }


    @Override
    public List<ClientesEntity> getAllClientes() {
        return clientesRepository.findAll();
    }

    @Override
    public List<ClientesEntity> findByNombreService(String nombre) {

        try {
            return clientesRepository.findByNombreContaining(nombre);
        }catch (ExceptionGeneral e){
            throw new ExceptionGeneral("No se encontraron resultados con ese nombre cliente");

        }
    }

    @Override
    public List<ClientesEntity> findByNombreAndNitService(String nombre, Long nit) {
        return clientesRepository.findByNombreAndNit(nombre, nit);
    }

    @Override
    public void deleteCliente(Long id) {
        clientesRepository.deleteById(id);
    }



    //todo  el maximo de carga es 25200 registros en archivo de excel
    @Override
    public void cargarClientesPorPlano(MultipartFile archivo) throws IOException {
        List<ClientesEntity> registrosTemporales = new ArrayList<>();

        if(errores.size()>0 || registrosTemporales.size()>0 ){
            errores.clear();
            registrosTemporales.clear();
        }

        String nombreArchivo = archivo.getOriginalFilename();
        //para respuesta en error:
        String[] columnasEsperadas = {
                "nit",
                "nombre",
                "dirección",
                "correoElectronico",
                "saldoUsado",
                "cupoTotal",
                "disponible",

        };
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
                throw new IllegalArgumentException("Tipo de archivo Excel no es compatible");
            }

            Sheet sheet = workbook.getSheetAt(0); // Suponiendo que los datos están en la primera hoja
            Row headerRow = sheet.getRow(0);
            if (headerRow != null && headerRow.getPhysicalNumberOfCells() > 0) {

                List<String> columnasEnArchivo = new ArrayList<>();


                for (Cell cell : headerRow) {
                    columnasEnArchivo.add(cell.getStringCellValue());
                }
                if (Arrays.equals(columnasEsperadas, columnasEnArchivo.toArray(new String[0]))) {
                    int rowIndex=0;
                    for (rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {

                       try {

                           Row row = sheet.getRow(rowIndex);
                           if (row == null) {
                               errores.add("Error al procesar la fila " + rowIndex + " no se pudo guardar esta vacia  ");
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
                               errores.add("Error al procesar la fila " + rowIndex + " no se pudo guardar vacia o null");

                               continue;
                           }

                           // Validar si la celda "nit" es nula o vacía
                           if (cell0 == null || cell0.getStringCellValue().trim().isEmpty()) {
                               errores.add("Error al procesar la fila " + rowIndex + ": El campo 'nit' está vacío o nulo.");
                               registrosFallidos++;
                               continue; // Salta la fila si "nit" no está presente
                           }


                           ClientesEntity cliente = new ClientesEntity();

                           String nitValue ="0";
                           Long  nitLong=Long.getLong(nitValue);
                           Optional<ClientesEntity> clientesEntityOptional= clientesRepository.findByNit(nitLong);

              if(!clientesEntityOptional.isPresent()){

                  if (cell0.getCellType() == CellType.NUMERIC) {
                      // Si la celda es numérica, obtén el valor como cadena
                      nitValue = String.valueOf((int) cell0.getNumericCellValue());
                  } else if (cell0.getCellType() == CellType.STRING) {
                      // Si la celda es una cadena, obtén el valor directamente
                      nitValue = cell0.getRichStringCellValue().getString();
                  } else {
                      // Tipo de celda no válido
                      errores.add("Error al procesar la fila " + rowIndex + ": El campo 'nit' debe ser numérico o de texto.");
                      registrosFallidos++;
                      continue; // Salta la fila si "nit" no es numérico o de texto
                  }


                  int maxLength = 255; // Longitud máxima permitida para la descripción
                  String nombreCliente=  row.getCell(1).getStringCellValue();
                  if (nombreCliente.length() > maxLength) {
                      nombreCliente = nombreCliente.substring(0, maxLength); // Acorta el texto si supera la longitud máxima
                  }




                  cliente.setNit(Long.parseLong(nitValue));
                  cliente.setNombre(nombreCliente);

                  cliente.setDirección(row.getCell(2).getStringCellValue());
                  cliente.setCorreoElectronico(row.getCell(3).getStringCellValue());
                  cliente.setSaldoUsado(ExcelUtils.getNumericCellValue(row.getCell(4)));
                  cliente.setCupoTotal(ExcelUtils.getNumericCellValue(row.getCell(5)));
                  cliente.setDisponible(ExcelUtils.getNumericCellValue(row.getCell(6)));

                  registrosTemporales.add(cliente);

              }else {
                  errores.add("El cliente ya existe fila:  "+ rowIndex );
                  registrosFallidos++;

              }

                       }catch (Exception e){
                           errores.add("Error al procesar la fila:  "+ rowIndex+ ": " + e.getMessage());
                           registrosFallidos++;

                       }

                    }


                        try {
                           clientesRepository.saveAll(registrosTemporales);
                            registrosExitosos+=registrosTemporales.size();
                        } catch (Exception e) {
                            // Manejar excepciones, por ejemplo, loguear el error
                            e.printStackTrace();
                            registrosFallidos++;
                            errores.add("Error al procesar la fila " +(rowIndex) + (registrosFallidos + registrosExitosos) + ": " + e.getMessage());
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




}


