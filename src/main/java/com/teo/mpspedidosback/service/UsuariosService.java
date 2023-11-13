package com.teo.mpspedidosback.service;


import com.teo.mpspedidosback.dto.UsuarioDtoResponse;
import com.teo.mpspedidosback.dto.UsuarioValidacionDtoRequest;
import com.teo.mpspedidosback.entity.GeneradorCodigoUnico;
import com.teo.mpspedidosback.entity.ProductosEntity;
import com.teo.mpspedidosback.entity.UsuariosEntity;
import com.teo.mpspedidosback.exception.ExceptionGeneral;
import com.teo.mpspedidosback.repository.IUsuariosRepository;
import com.teo.mpspedidosback.service.api.IUsuariosService;
import org.apache.bcel.generic.NEW;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UsuariosService implements IUsuariosService {

    public static int registrosExitosos;
    public static int registrosFallidos;
    public static List<String> errores = new ArrayList<>();
    @Autowired
    private IUsuariosRepository usuariosRepository;

    @Override
    public void createUser(UsuariosEntity usuariosEntity) {
        usuariosRepository.save(usuariosEntity);
    }

    @Override
    public void updateUser(UsuariosEntity usuariosEntity) {
        usuariosRepository.save(usuariosEntity);
    }

    @Override
    public UsuariosEntity getUser(Long id) {
        Optional<UsuariosEntity> usuariosEntityOptional=  usuariosRepository.findById(id);
        if(!usuariosEntityOptional.isPresent()){
            throw new ExceptionGeneral("El usuario no existe en la base de datos");
        }
        return usuariosEntityOptional.get();
    }

    @Override
    public List<UsuariosEntity> getAllUser() {
        return usuariosRepository.findAll();
    }

    @Override
    public void deleteUser(Long codigo) {
        usuariosRepository.deleteById(codigo);
    }


    @Override
    public void cargarUsuariosPorPlano(MultipartFile archivo) throws IOException {




        List<UsuariosEntity> registrosTemporales = new ArrayList<>();
        if(registrosTemporales.size()>0) {
            registrosTemporales.clear();
        }
        String nombreArchivo = archivo.getOriginalFilename();
        String[] columnasEsperad = {
                "nombreUsuario",
                "usuario",
                "contrasena",
                "rol",
        };
        //para respuesta en error:
        String columnasEsperadasSt = String.join(", ", columnasEsperad);
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
                if (Arrays.equals(columnasEsperad, columnasEnArchivo.toArray(new String[0]))) {


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

                            UsuariosEntity usuariosEntity = new UsuariosEntity();

                            usuariosEntity.setNombreUsuario(row.getCell(0).getStringCellValue());

                             Optional<UsuariosEntity>usuariosEntityOptional= usuariosRepository.findByUsuario(usuariosEntity.getUsuario());

                            if(!usuariosEntityOptional.isPresent()) {
                                usuariosEntity.setUsuario(row.getCell(1).getStringCellValue());
                                usuariosEntity.setPassword(row.getCell(2).getStringCellValue());
                                usuariosEntity.setRol(row.getCell(3).getStringCellValue());


                                registrosExitosos+=1;
                                registrosTemporales.add(usuariosEntity);

                            } else{
                                registrosFallidos++;
                                errores.add("Error el usuario ya existe en la bd  fila :" +rowIndex );

                            }


                        }catch (Exception e){
                            registrosFallidos++;
                            errores.add("Error al procesar la fila :" +rowIndex  + ": " + e.getMessage());


                        }
                    }


                    try {
                        usuariosRepository.saveAll(registrosTemporales);
                        if(registrosExitosos==0){

                            registrosExitosos+=registrosTemporales.size();
                        }

                    } catch (Exception e) {
                        // Manejar excepciones, por ejemplo, loguear el error
                        e.printStackTrace();
                        registrosFallidos++;
                        errores.add("Error al procesar la fila " + (registrosFallidos + registrosExitosos) + ": " + e.getMessage());
                    }



                } else {
                    // Los encabezados no coinciden, muestra un mensaje de error
                    throw new IllegalArgumentException("La estructura de las columnas no corresponde. Se esperaba: " + columnasEsperadasSt + " y el archivo tiene: " + String.join(", ", columnasEnArchivo));

                }}

        }catch(IOException e){
            // Manejar errores de lectura del archivo
            e.printStackTrace();
            throw new RuntimeException("Error de lectura del archivo valida los campos ingresados", e);
        }

    }

    @Override
    public UsuarioDtoResponse getfindByUsuario(String usuario) {

       Optional<UsuariosEntity> usuariosEntityOptional= usuariosRepository.findByUsuario(usuario);
     if(!usuariosEntityOptional.isPresent()) {
         throw new ExceptionGeneral("El usuario no registra en la base de datos");
     }
        UsuarioDtoResponse  usuarioDtoResponse= new  UsuarioDtoResponse ();

        usuarioDtoResponse.setNombreUsuario(usuariosEntityOptional.get().getNombreUsuario());
        usuarioDtoResponse.setRol(usuariosEntityOptional.get().getRol());
        usuarioDtoResponse.setUsuario(usuariosEntityOptional.get().getUsuario());
        
        return usuarioDtoResponse;
    }

    @Override
    public void validacionUser(UsuarioValidacionDtoRequest usuarioValidacionDtoRequest) {


       Optional<UsuariosEntity> usuariosEntity=  usuariosRepository.findByUsuario(usuarioValidacionDtoRequest.getUsuario());


        if(usuariosEntity.get().getUsuario().equals(usuarioValidacionDtoRequest.getUsuario())
                &&
        usuariosEntity.get().getPassword().equals(usuarioValidacionDtoRequest.getPassword()))
        {
            System.out.println("Cliente autenticado con exito");

        }else {

            throw new ExceptionGeneral("El cliente no fue autenticado, valida usuario y password");
        }

    }

}
