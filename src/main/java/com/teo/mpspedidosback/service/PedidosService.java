package com.teo.mpspedidosback.service;


import com.teo.mpspedidosback.dto.*;
import com.teo.mpspedidosback.entity.*;
import com.teo.mpspedidosback.exception.ExceptionGeneral;
import com.teo.mpspedidosback.repository.IClientesRepository;
import com.teo.mpspedidosback.repository.IPedidosRepository;
import com.teo.mpspedidosback.repository.IProductosRepository;
import com.teo.mpspedidosback.service.api.IEmailService;
import com.teo.mpspedidosback.service.api.IPedidosService;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.mail.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import javax.mail.internet.*;

import java.text.NumberFormat;
import java.util.Locale;

import static com.teo.mpspedidosback.service.EmailService.configureEmailSession;

@Service
public class PedidosService implements IPedidosService {



    @Autowired
    private IPedidosRepository pedidosRepository;
    @Autowired
    private IClientesRepository clientesRepository;
    @Autowired
    private IProductosRepository productosRepository;
    @Autowired
    private IEmailService emailService;


    @Override
    public void createPedidos(PedidoDtoRequest pedidoDtoRequest) {

       Long idCliente= pedidoDtoRequest.getIdCliente();
       String codigoInterno= pedidoDtoRequest.getCodigoInterno();
       String estado= pedidoDtoRequest.getEstado();


        Integer numeroPedido=conteoPedidos()+1;
       double porcentaIva=0.19;


        List<PedidosEntity> pedidosEntityList = new ArrayList<>();

        List<ProductosDtoRequest> productos = pedidoDtoRequest.getListaProductos();

              for (ProductosDtoRequest producto : productos) {

                  Long idProducto = producto.getId();
                  Integer cantidad = producto.getCantidad();

                  Integer valorUnitario= producto.getValorUnitario();

                  try {
                      Optional<ClientesEntity> clientesEntity = clientesRepository.findByNit(idCliente);
                      if (!clientesEntity.isPresent()) {
                          throw new ExceptionGeneral("El cliente no registra en la base de datos");
                      }

                      Long nit=  clientesEntity.get().getNit();
                      String nombreComercial=  clientesEntity.get().getNombre();
                      String correo=clientesEntity.get().getCorreoElectronico();

                      Optional<ProductosEntity>  productosEntity=  productosRepository.findById(idProducto);
                      if(!productosEntity.isPresent()){
                          throw new ExceptionGeneral("el producto no registra en la base de datos ");

                      }
                      String clasificacionTributaria=  productosEntity.get().getClasificaciontributaria();

                      var ivaPorPedido=0.0;
                      if("GRAVADO".equals(clasificacionTributaria)){
                          ivaPorPedido = valorUnitario * porcentaIva * cantidad;

                      }


                      var valorTotalPorPro= Long.valueOf (valorUnitario * cantidad);
                      var valorNetoPorProd= valorTotalPorPro + ivaPorPedido;

                      String numeroParte= productosEntity.get().getNumerodeparte();

                      String descripcion= productosEntity.get().getDescripcion();
                      String tipoNegocio= productosEntity.get().getTipoDeNegocio();
                      String marca=  productosEntity.get().getMarca();
                      String color=  productosEntity.get().getColor();
                      String stock =  productosEntity.get().getStock();
                      String preciominimocop= productosEntity.get().getPreciominimocop();
                      String preciominimousd= productosEntity.get().getPreciominimocop();

                      PedidosEntity pedidosEntity= new PedidosEntity(nombreComercial,nit,
                              valorUnitario
                              ,ivaPorPedido,estado,
                              correo,codigoInterno,
                              numeroParte,cantidad,
                              descripcion,
                              marca,color,stock,clasificacionTributaria,
                              numeroPedido , tipoNegocio,
                              preciominimocop, preciominimousd ,valorNetoPorProd, valorTotalPorPro
                      );

                      pedidosEntityList.add(pedidosEntity);
                  } catch (Exception e) {
                      throw new ExceptionGeneral("Nit  duplicado en la base de datos clientes o sin productos existen en bd "+e.getMessage());
                  }


              }
        pedidosRepository.saveAll(pedidosEntityList);

    }
    @Override
    public void updatePedidosConfirmacion(PedidoConfirmarDtoRequest pedidoConfirmarDtoRequest) {


        PedidosConfiDtoRequest pedidosConfiDtoRequest= pedidoConfirmarDtoRequest.getDatosUpdate();
        List<PedidosEntity>   pedidosEntityList= pedidosRepository.findByCodigoInterno( pedidoConfirmarDtoRequest.getCodigoInterno());
        List<PedidosEntity> pedidosEntityListUpdate = new ArrayList<>();

        for (PedidosEntity pedido :pedidosEntityList) {

            pedido.setPersonaContacto(pedidosConfiDtoRequest.getPersonaContacto());
            pedido.setDireccion(pedidosConfiDtoRequest.getDireccion());
            pedido.setCelular(pedidosConfiDtoRequest.getCelular());
            pedido.setTelefonoFijo(pedidosConfiDtoRequest.getTelefonoFijo());
            pedido.setCorreoElectronico(pedidosConfiDtoRequest.getCorreoElectronico());
            pedido.setValorTotalPedido(pedidosConfiDtoRequest.getValorTotalPedido());
            pedido.setNetoApagar(pedidosConfiDtoRequest.getNetoApagar());
            pedido.setIvaTotalPed(pedidosConfiDtoRequest.getIvaTotalPed());
            pedido.setFormaDePago(pedidosConfiDtoRequest.getFormaPago());
            pedido.setObservaciones(pedidosConfiDtoRequest.getObservaciones());
            pedido.setEstado(pedidoConfirmarDtoRequest.getEstado());
            pedido.setEvento(pedidoConfirmarDtoRequest.getEvento());
            pedido.setFechaCreacion(pedidosConfiDtoRequest.getFechaCreacion());
            pedido.setCorreoComercial(pedidoConfirmarDtoRequest.getCorreoAsesor());

            pedidosEntityListUpdate.add(pedido);

        }

        pedidosRepository.saveAll(pedidosEntityListUpdate);
        try {
        PedidoEmailCarteraDtoRequest pedidoEmailCarteraDtoRequest =  new PedidoEmailCarteraDtoRequest ();

        pedidoEmailCarteraDtoRequest.setCodigoInterno(pedidoConfirmarDtoRequest.getCodigoInterno());
        pedidoEmailCarteraDtoRequest.setEstado(pedidoConfirmarDtoRequest.getEstado());
        pedidoEmailCarteraDtoRequest.setCorreo("carteramatch@mps.com.co");

            enviarCorreoCartera(pedidoEmailCarteraDtoRequest);
        }catch (Exception e){

            throw new ExceptionGeneral("Error con el envio del correo a cartera "+ e.getMessage());

        }

    }

    @Override
    public void updatePedidos(PedidoDtoUpdateRequest pedidoDtoUpdateRequest) {

        Long idCliente= pedidoDtoUpdateRequest.getIdCliente();
        String codigoInterno= pedidoDtoUpdateRequest.getCodigoInterno();
        String estado= pedidoDtoUpdateRequest.getEstado();

        double porcentaIva=0.19;

        List<PedidosEntity> pedidosEntityListActNumeroPed= pedidosRepository.findByCodigoInterno(codigoInterno);
        Integer numeroPedido=pedidosEntityListActNumeroPed.get(0).getNumeroPedido();
        Optional<ClientesEntity>  clientesEntity =  clientesRepository.findByNit(idCliente);
        if(!clientesEntity.isPresent()){
            throw new ExceptionGeneral("El cliente no registra en la base de datos ");
        }
        List<PedidosEntity> pedidosEntityList = new ArrayList<>();
        List<ProductosDtoRequest> productos = pedidoDtoUpdateRequest.getListaProductos();

        for (ProductosDtoRequest producto : productos) {

            Long idProducto = producto.getId();
            Integer cantidad = producto.getCantidad();
            Integer valorUnitario= producto.getValorUnitario();
            Long nit=  clientesEntity.get().getNit();
            String nombreComercial=  clientesEntity.get().getNombre();
            String correo=clientesEntity.get().getCorreoElectronico();

          ;
            Optional<ProductosEntity>  productosEntity=  productosRepository.findById(idProducto);
            if(!productosEntity.isPresent()){
                throw new ExceptionGeneral("el producto no registra en la base de datos ");
            }
            String clasificacionTributaria=  productosEntity.get().getClasificaciontributaria();

            var iva=0.0;
            if("GRAVADO".equals(clasificacionTributaria)){
                iva = valorUnitario * porcentaIva * cantidad;
            }
            var valorTotalPorPro= Long.valueOf (valorUnitario * cantidad);
            var valorNetoPorProd= valorTotalPorPro+iva;

            String numeroParte= productosEntity.get().getNumerodeparte();

            String descripcion= productosEntity.get().getDescripcion();
            String tipoNegocio= productosEntity.get().getTipoDeNegocio();
            String marca=  productosEntity.get().getMarca();
            String color=  productosEntity.get().getColor();
            String stock =  productosEntity.get().getStock();
            String preciominimocop= productosEntity.get().getPreciominimocop();
            String preciominimousd= productosEntity.get().getPreciominimocop();

            PedidosEntity pedidosEntity= new PedidosEntity(nombreComercial,nit,
                    valorUnitario
                    ,iva,estado,
                    correo,codigoInterno,
                    numeroParte,cantidad,
                    descripcion,
                    marca,color,stock,clasificacionTributaria,
                    numeroPedido , tipoNegocio,
                    preciominimocop, preciominimousd ,valorNetoPorProd, valorTotalPorPro
            );

            pedidosEntityList.add(pedidosEntity);
        }

        pedidosRepository.saveAll(pedidosEntityList);
    }

    @Override
    public PedidosEntity getPedidos(Long id) {
        Optional<PedidosEntity> optionalPedidosEntity = pedidosRepository.findById(id);
        if(!optionalPedidosEntity.isPresent()){
            throw new ExceptionGeneral("El pedido no existe");
        }
        return optionalPedidosEntity.get();
    }
    @Override
    public List<PedidoDtoResponse> findByCodigoInternServ(String codigoInterno) {

        // Realiza la lógica para buscar el pedido por su código interno
        List<PedidosEntity> pedidoEntities = pedidosRepository.findByCodigoInterno(codigoInterno); // Suponiendo que tienes un repositorio de pedidos
        // Utiliza stream y map para mapear cada entidad a su correspondiente DTO
        List<PedidoDtoResponse> pedidoDtoResponses = pedidoEntities.stream()
                .map(pedidoEntity -> {
                    PedidoDtoResponse pedidoDtoResponse = new PedidoDtoResponse();
                    pedidoDtoResponse.setId(pedidoEntity.getId());
                    pedidoDtoResponse.setDni(pedidoEntity.getDni());
                    pedidoDtoResponse.setNombreComercial(pedidoEntity.getNombreComercial());
                    pedidoDtoResponse.setTipoDocumento(pedidoEntity.getTipoDocumento());
                    pedidoDtoResponse.setTipoDeNegocio(pedidoEntity.getTipoDeNegocio());
                    pedidoDtoResponse.setTotalIva(pedidoEntity.getIvaTotalPed());
                    pedidoDtoResponse.setNetoApagar((pedidoEntity.getNetoApagar())); // Ajusta el campo netoApagar según corresponda
                    pedidoDtoResponse.setFormaDePago(pedidoEntity.getFormaDePago()); // Ajusta el campo formaDePago según corresponda
                    pedidoDtoResponse.setEstado(pedidoEntity.getEstado());
                    pedidoDtoResponse.setPersonaContacto(pedidoEntity.getPersonaContacto());
                    pedidoDtoResponse.setDireccion(pedidoEntity.getDireccion());
                    pedidoDtoResponse.setCelular(pedidoEntity.getCelular());
                    pedidoDtoResponse.setTelefonoFijo(pedidoEntity.getTelefonoFijo());
                    pedidoDtoResponse.setCorreoElectronico(pedidoEntity.getCorreoElectronico());
                    pedidoDtoResponse.setCodigoInterno(pedidoEntity.getCodigoInterno());
                    pedidoDtoResponse.setNumeroPedido(pedidoEntity.getNumeroPedido() );
                    pedidoDtoResponse.setNumerodeparte(pedidoEntity.getNumerodeparte());
                    pedidoDtoResponse.setMarca(pedidoEntity.getMarca());
                    pedidoDtoResponse.setDescripcion(pedidoEntity.getDescripcion());
                    pedidoDtoResponse.setCantidad(pedidoEntity.getCantidad());
                    pedidoDtoResponse.setValorUnitario(pedidoEntity.getValorUnitario());
                    pedidoDtoResponse.setIva(pedidoEntity.getIvaPorProd());
                    pedidoDtoResponse.setStock(pedidoEntity.getStock());
                    pedidoDtoResponse.setPreciominimocop(pedidoEntity.getPreciominimocop());
                    pedidoDtoResponse.setPreciominimousd(pedidoEntity.getPreciominimousd());
                    pedidoDtoResponse.setValorNetoPorProd(pedidoEntity.getValorNetoPorProd());
                    pedidoDtoResponse.setValorTotalPorPro(pedidoEntity.getValorTotalPorPro());

                    return pedidoDtoResponse;
                })
                .collect(Collectors.toList());

        return pedidoDtoResponses;
    }

    @Override
    public List<PedidosEntity> getAllPedidos() {
        return pedidosRepository.findAll();
    }

    @Override
    public List<PedidoAcumuladoDtoResponse> getAllPedidosAcumulados() {

        List<PedidosEntity> pedidosEntityList = pedidosRepository.findAll();
        List<PedidoAcumuladoDtoResponse> resultados = new ArrayList<>();

        for (PedidosEntity pedidos : pedidosEntityList) {
            boolean encontrado = false;
            for (PedidoAcumuladoDtoResponse pedidoAcumulado : resultados) {
                if (pedidoAcumulado.getCodigoInterno().equals(pedidos.getCodigoInterno())) {
                    pedidoAcumulado.setCantidad(pedidoAcumulado.getCantidad() + pedidos.getCantidad());
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                // No se encontró un pedido acumulado con el mismo código interno, crea uno nuevo
                PedidoAcumuladoDtoResponse nuevoPedidoAcumulado = new PedidoAcumuladoDtoResponse();
                nuevoPedidoAcumulado.setCodigoInterno(pedidos.getCodigoInterno());
                nuevoPedidoAcumulado.setId(pedidos.getId());
                nuevoPedidoAcumulado.setDni(pedidos.getDni());
                nuevoPedidoAcumulado.setNombreComercial(pedidos.getNombreComercial());
                nuevoPedidoAcumulado.setFormaDePago(pedidos.getFormaDePago());
                nuevoPedidoAcumulado.setStock(pedidos.getStock());
                nuevoPedidoAcumulado.setNumeroPedido(pedidos.getNumeroPedido());
                nuevoPedidoAcumulado.setValorTotal(pedidos.getValorTotalPedido());
                nuevoPedidoAcumulado.setTotalIva(pedidos.getIvaTotalPed());
                nuevoPedidoAcumulado.setNetoApagar(pedidos.getNetoApagar());
                nuevoPedidoAcumulado.setEstado(pedidos.getEstado());
                nuevoPedidoAcumulado.setCantidad(pedidos.getCantidad());
                nuevoPedidoAcumulado.setFechaCreación(pedidos.getFechaCreacion());

                resultados.add(nuevoPedidoAcumulado);
            }
        }

        return resultados;

    }



    @Override
    public void deletePedidos(Long id) {
            pedidosRepository.deleteById(id);
    }

    @Override
    public void deleteByCodigoPedido(String codigo) {
        List<PedidosEntity>pedidosEntityList = pedidosRepository.findByCodigoInterno(codigo);

        for (PedidosEntity pedidoEliminar : pedidosEntityList) {
            pedidosRepository.delete(pedidoEliminar);
        }

    }

    @Override
    public void enviarCorreo(PedidoCamEstadoDtoRequest pedidoCamEstadoDtoRequest) {

        Locale localeColombia = new Locale("es", "CO");
        NumberFormat formato = NumberFormat.getNumberInstance(localeColombia);
        formato.setMaximumFractionDigits(2);

        // Configurar el número de decimales deseados
        formato.setMaximumFractionDigits(2);


        String estado=  pedidoCamEstadoDtoRequest.getEstado();
        String correoCliente="";


        String correoAsesor="";


        Integer orden=0;

         List<PedidosEntity> pedidosEntityList= pedidosRepository.findByCodigoInterno(pedidoCamEstadoDtoRequest.getCodigoInterno());


         List<PedidosEntity> savePedidosEntity= new ArrayList<>();
         List<PedidoEmailDtoResponse> productos = new ArrayList<>() ;
         List<PedidoEmailDBDtoResponse> datosBasicos = new ArrayList<>() ;


         if(pedidosEntityList.isEmpty()){
             throw new ExceptionGeneral("El codigo Interno Proporcionado No posee registros ");
         }


        // asunto

        for ( PedidosEntity pedidosEntity  :pedidosEntityList) {
            pedidosEntity.setEstado(estado);

            orden=pedidosEntity.getNumeroPedido();


            PedidoEmailDBDtoResponse pedidoEmailDBDtoResponse=new PedidoEmailDBDtoResponse();
            pedidoEmailDBDtoResponse.setDni(pedidosEntity.getDni());
            pedidoEmailDBDtoResponse.setNombreComercial(pedidosEntity.getNombreComercial());
            pedidoEmailDBDtoResponse.setNetoApagar(pedidosEntity.getNetoApagar());
            pedidoEmailDBDtoResponse.setCorreoElectronico(pedidosEntity.getCorreoElectronico());
            pedidoEmailDBDtoResponse.setCelular(pedidosEntity.getCelular());
            pedidoEmailDBDtoResponse.setDireccion(pedidosEntity.getDireccion());
            pedidoEmailDBDtoResponse.setFormaDePago(pedidosEntity.getFormaDePago());
            pedidoEmailDBDtoResponse.setPersonaContacto(pedidosEntity.getPersonaContacto());
            pedidoEmailDBDtoResponse.setTelefonoFijo(pedidosEntity.getTelefonoFijo());
            pedidoEmailDBDtoResponse.setValorTotal(pedidosEntity.getValorTotalPedido());

            correoCliente=pedidosEntity.getCorreoElectronico();
            correoAsesor=pedidosEntity.getCorreoComercial();

            if (!datosBasicos.contains(pedidoEmailDBDtoResponse)) {
                datosBasicos.add(pedidoEmailDBDtoResponse);
            }

            //productos
            PedidoEmailDtoResponse pedidoEmailDtoResponse= new PedidoEmailDtoResponse();
            pedidoEmailDtoResponse.setNumeroPedido(pedidosEntity.getNumeroPedido());
            pedidoEmailDtoResponse.setCantidad(pedidosEntity.getCantidad());
            pedidoEmailDtoResponse.setMarca(pedidosEntity.getMarca());
            pedidoEmailDtoResponse.setNumerodeparte(pedidosEntity.getNumerodeparte());
            pedidoEmailDtoResponse.setNombreArticulo(pedidosEntity.getDescripcion());
            pedidoEmailDtoResponse.setValorUnitario(pedidosEntity.getValorUnitario());

            productos.add(pedidoEmailDtoResponse);
            //guardar nuevo estado
            savePedidosEntity.add(pedidosEntity);
        }
        pedidosRepository.saveAll(savePedidosEntity);
        EmailEntity emailEntity= new EmailEntity();


        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html><body>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Buen día,</p>");
        stringBuilder.append("<br>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Enviamos detalle del pedido:</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>");

        for (PedidoEmailDBDtoResponse datos: datosBasicos) {
            stringBuilder.append("<br>");
            stringBuilder.append("<p style='font-size: 13px; color: #333;'>Nit o CC = " + datos.getDni() + "</p>");
            stringBuilder.append("<p style='font-size: 13px; color: #333;'>Nombre Comercial = " + datos.getNombreComercial() + "</p>");
           stringBuilder.append("<p style='font-size: 13px; color: #333;'>Correo Electrónico = " + datos.getCorreoElectronico() + "</p>");
            stringBuilder.append("<p style='font-size: 13px; color: #333;'>Celular = " + datos.getCelular() + "</p>");
            stringBuilder.append("<p style='font-size: 13px; color: #333;'>Dirección = " + datos.getDireccion() + "</p>");
            stringBuilder.append("<p style='font-size: 13px; color: #333;'>Forma de Pago = " + datos.getFormaDePago() + "</p>");
            stringBuilder.append("<p style='font-size: 13px; color: #333;'>Persona de Contacto = " + datos.getPersonaContacto() + "</p>");
            stringBuilder.append("<p style='font-size: 13px; color: #333;'>Teléfono Fijo = " + datos.getTelefonoFijo() + "</p>");
            stringBuilder.append("<p style='font-size: 13px; color: #333;'>Valor Total = $ " + formato.format(datos.getValorTotal()) + "</p>");
            stringBuilder.append("<p style='font-size: 13px; color: #333;'>Neto a pagar = $ " + formato.format(datos.getNetoApagar())  + "</p>");
            stringBuilder.append("<br>");
            stringBuilder.append("</p>");

            correoCliente=datos.getCorreoElectronico();
        }
            stringBuilder.append("<p style='font-size: 13px; color: #333;'>Datos productos :</p>");
        for (PedidoEmailDtoResponse producto:productos) {


                 stringBuilder.append("<p style='font-size: 13px; color: #333;'>N° Pedido: " + producto.getNumeroPedido() + "</p>");
                stringBuilder.append("<p style='font-size: 13px; color: #333;'>N° de Parte: " + producto.getNumerodeparte() + "</p>");
                stringBuilder.append("<p style='font-size: 13px; color: #333;'>Nombre del Artículo: " + producto.getNombreArticulo() + "</p>");
                stringBuilder.append("<p style='font-size: 13px; color: #333;'>Marca: " + producto.getMarca() + "</p>");
                stringBuilder.append("<p style='font-size: 13px; color: #333;'>Cantidad: " + producto.getCantidad() + "</p>");
                stringBuilder.append("<p style='font-size: 13px; color: #333;'>Valor Unitario: " + formato.format(producto.getValorUnitario()) + "</p>");
                stringBuilder.append("<br>");

                stringBuilder.append("</body></html>");

            }


        if(!correoCliente.equals("") ){
            emailEntity.setDestinatario(correoCliente);
        }else {

            throw new ExceptionGeneral("El correo del cliente no es valido, (no" +
                    " puede llegar vacio ni separadao por ;: solo por ,)");
        }


        if(estado.equals("aprobado")) {
            emailEntity.setAsunto("MPS Confirmada Solicitud de Orden N° : " + orden);
        }else{
            emailEntity.setAsunto("MPS (Cancelado) Solicitud de Orden N° : " + orden);
        }
        emailEntity.setCuerpoCorreo(stringBuilder.toString())   ;

        try {
            // Crear una sesión de correo electrónico
            Session emailSession = configureEmailSession(emailEntity);

            // Crear el mensaje de correo electrónico utilizando la sesión y los datos de la solicitud
            Message emailMessage = emailService.createEmail(emailSession, emailEntity);

            List<String>destinatariosCco=new ArrayList<>();

            if(estado.equals("aprobado") ){
                destinatariosCco.add("ejecutivosmatch@mps.com.co");
            }


            if(!correoAsesor.equals("") ){
                destinatariosCco.add(correoAsesor);
            }

                 // Configurar copia oculta (CCO o BCC)
            if (destinatariosCco != null && !destinatariosCco.isEmpty()) {
                Address[] ccoRecipients = new Address[destinatariosCco.size()];
                for (int i = 0; i < destinatariosCco.size(); i++) {
                    ccoRecipients[i] = new InternetAddress(destinatariosCco.get(i));
                }
                emailMessage.setRecipients(Message.RecipientType.BCC, ccoRecipients);
            }

            // Enviar el correo electrónico
            emailService.sendEmail(emailMessage);

        } catch (Exception e) {
            e.printStackTrace();

        }

}


    @Override
    public void enviarCorreoCartera(PedidoEmailCarteraDtoRequest pedidoEmailCarteraDtoRequest) {


        Locale localeColombia = new Locale("es", "CO");
        NumberFormat formato = NumberFormat.getNumberInstance(localeColombia);
        formato.setMaximumFractionDigits(2);
        Integer orden=0;
        // Configurar el número de decimales deseados
        formato.setMaximumFractionDigits(2);


        String carteraOpcional="";
        String estadoCorreo= pedidoEmailCarteraDtoRequest.getEstado();
        String correoCartera=pedidoEmailCarteraDtoRequest.getCorreo();



        List<PedidosEntity> pedidosEntityList= pedidosRepository.findByCodigoInterno(pedidoEmailCarteraDtoRequest.getCodigoInterno());



        List<PedidosEntity> savePedidosEntity= new ArrayList<>();
        List<PedidoEmailDtoResponse> productos = new ArrayList<>() ;
        List<PedidoEmailDBDtoResponse> datosBasicos = new ArrayList<>() ;

    try{
    if(pedidosEntityList.isEmpty()){
        throw new ExceptionGeneral("El codigo Interno Proporcionado No posee registros ");
    }


    // asunto

    for ( PedidosEntity pedidosEntity  :pedidosEntityList) {
        pedidosEntity.setEstado(estadoCorreo);

        PedidoEmailDBDtoResponse pedidoEmailDBDtoResponse=new PedidoEmailDBDtoResponse();
        pedidoEmailDBDtoResponse.setDni(pedidosEntity.getDni());
        pedidoEmailDBDtoResponse.setNombreComercial(pedidosEntity.getNombreComercial());
        pedidoEmailDBDtoResponse.setNetoApagar(pedidosEntity.getNetoApagar());
        pedidoEmailDBDtoResponse.setCorreoElectronico(pedidosEntity.getCorreoElectronico());
        pedidoEmailDBDtoResponse.setCelular(pedidosEntity.getCelular());
        pedidoEmailDBDtoResponse.setDireccion(pedidosEntity.getDireccion());
        pedidoEmailDBDtoResponse.setFormaDePago(pedidosEntity.getFormaDePago());
        pedidoEmailDBDtoResponse.setPersonaContacto(pedidosEntity.getPersonaContacto());
        pedidoEmailDBDtoResponse.setTelefonoFijo(pedidosEntity.getTelefonoFijo());
        pedidoEmailDBDtoResponse.setValorTotal(pedidosEntity.getValorTotalPedido());

        orden=pedidosEntity.getNumeroPedido();

        if (!datosBasicos.contains(pedidoEmailDBDtoResponse)) {
            datosBasicos.add(pedidoEmailDBDtoResponse);
        }

        //productos
        PedidoEmailDtoResponse pedidoEmailDtoResponse= new PedidoEmailDtoResponse();
        pedidoEmailDtoResponse.setNumeroPedido(pedidosEntity.getNumeroPedido());
        pedidoEmailDtoResponse.setCantidad(pedidosEntity.getCantidad());
        pedidoEmailDtoResponse.setMarca(pedidosEntity.getMarca());
        pedidoEmailDtoResponse.setNumerodeparte(pedidosEntity.getNumerodeparte());
        pedidoEmailDtoResponse.setNombreArticulo(pedidosEntity.getDescripcion());
        pedidoEmailDtoResponse.setValorUnitario(pedidosEntity.getValorUnitario());
        productos.add(pedidoEmailDtoResponse);

        savePedidosEntity.add(pedidosEntity);
    }
    pedidosRepository.saveAll(savePedidosEntity);
    EmailEntity emailEntity= new EmailEntity();


    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<html><body>");
    stringBuilder.append("<p style='font-size: 13px; color: #333;'>Buen día,</p>");
    stringBuilder.append("<br>");
    stringBuilder.append("<p style='font-size: 13px; color: #333;'>Enviamos detalle del pedido:</p>");
    stringBuilder.append("<p style='font-size: 13px; color: #333;'>");

    for (PedidoEmailDBDtoResponse datos: datosBasicos) {
        stringBuilder.append("<br>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Nit o CC = " + datos.getDni() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Nombre Comercial = " + datos.getNombreComercial() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Correo Electrónico = " + datos.getCorreoElectronico() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Celular = " + datos.getCelular() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Dirección = " + datos.getDireccion() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Forma de Pago = " + datos.getFormaDePago() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Persona de Contacto = " + datos.getPersonaContacto() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Teléfono Fijo = " + datos.getTelefonoFijo() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Valor Total = $ " +formato.format( datos.getValorTotal())+ "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Neto a pagar = $ " + formato.format(datos.getNetoApagar())  + "</p>");
        stringBuilder.append("<br>");
        stringBuilder.append("</p>");


    }
    stringBuilder.append("<p style='font-size: 13px; color: #333;'>Datos productos :</p>");
    for (PedidoEmailDtoResponse producto:productos) {


        stringBuilder.append("<p style='font-size: 13px; color: #333;'>N° Pedido: " + producto.getNumeroPedido() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>N° de Parte: " + producto.getNumerodeparte() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Nombre del Artículo: " + producto.getNombreArticulo() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Marca: " + producto.getMarca() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Cantidad: " + producto.getCantidad() + "</p>");
        stringBuilder.append("<p style='font-size: 13px; color: #333;'>Valor Unitario: " + formato.format(producto.getValorUnitario()) + "</p>");
        stringBuilder.append("<br>");
        stringBuilder.append("</body></html>");

    }


    if(!correoCartera.equals("") ){

        if ( esCorreoElectronicoValido(correoCartera)) {
            emailEntity.setDestinatario(correoCartera);
        } else {
            throw new ExceptionGeneral("El correo del Cartera  no es valido en su estructura)");
        }


    }else {

        throw new ExceptionGeneral("El correo del Cartera  no es valido, (no" +
                " puede llegar vacio ni separadao por ;: solo por ,)");
    }


    if(estadoCorreo.equals("Confirmado")) {
        emailEntity.setAsunto("MPS (PendienteAprobacion) Solicitud de Orden N° : " + orden);
    }else{
        emailEntity.setAsunto("MPS (EstadoErrado) Solicitud de Orden N° : " + orden);
    }
    emailEntity.setCuerpoCorreo(stringBuilder.toString())   ;


        try {
            // Crear una sesión de correo electrónico
            Session emailSession = configureEmailSession(emailEntity);

            // Crear el mensaje de correo electrónico utilizando la sesión y los datos de la solicitud
            Message emailMessage = emailService.createEmail(emailSession, emailEntity);

            List<String>destinatariosCco=new ArrayList<>();

           // destinatariosCco.add("or4846@gmail.com");
            if(!carteraOpcional.equals("") ){
                destinatariosCco.add(correoCartera);
            }

            // Configurar copia oculta (CCO o BCC)
            if (destinatariosCco != null && !destinatariosCco.isEmpty()) {
                Address[] ccoRecipients = new Address[destinatariosCco.size()];
                for (int i = 0; i < destinatariosCco.size(); i++) {
                    ccoRecipients[i] = new InternetAddress(destinatariosCco.get(i));
                }
                emailMessage.setRecipients(Message.RecipientType.BCC, ccoRecipients);
            }

            // Enviar el correo electrónico
            emailService.sendEmail(emailMessage);

        } catch (Exception e) {
            e.printStackTrace();

        }


        }catch (Exception e){

            throw new IllegalArgumentException("error Envio correo los valores totales  esta null   o el correo esta mal escrito ",  e);

        }
    }


    public static boolean esCorreoElectronicoValido(String correo) {
        String expresionRegularCorreo = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pat = Pattern.compile(expresionRegularCorreo);
        Matcher mat = pat.matcher(correo);
        return mat.matches();
    }

    @Override
    public Integer conteoPedidos() {

        List<PedidosEntity> pedidosEntityList = pedidosRepository.findAll();
        List<PedidosEntity> pedidosUnicos = new ArrayList<>();
        List<PedidosEntity> consultaByTope = new ArrayList<>();

        for (PedidosEntity pedidos : pedidosEntityList) {
            boolean encontrado = false;
            for (PedidosEntity pedidoAcumulado : pedidosUnicos) {
                if (pedidoAcumulado.getCodigoInterno().equals(pedidos.getCodigoInterno())) {

                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                // No se encontró un pedido acumulado con el mismo código interno, crea uno nuevo
                PedidosEntity nuevoPedidoAcumulado = new PedidosEntity();
                nuevoPedidoAcumulado.setCodigoInterno(pedidos.getCodigoInterno());

                pedidosUnicos.add(nuevoPedidoAcumulado);
            }
        }

            try {
                Integer maxNumeroPedido = pedidosRepository.findMaxNumeroPedido();
                if (maxNumeroPedido > 0) {

                    for (int i = maxNumeroPedido + 1; i <= maxNumeroPedido + 50; i++) {
                        boolean existe = pedidosRepository.existsByNumeroPedido(i);
                        if (!existe) {
                            return i - 1;
                        }
                    }
                }
            }catch ( Exception e){

                return 1;
            }

        return null;
    }

    @Override
    public List<PedidoAcumuladoDtoResponse> calcularSumaPedidosSuperiorAValor(Integer valor) {

        //todo solamente sumar los aprobados

        List<PedidosEntity> pedidosEntityList = pedidosRepository.findAll();
        List<PedidoAcumuladoDtoResponse> resultados = new ArrayList<>();
        List<PedidoAcumuladoDtoResponse> consultaByTope = new ArrayList<>();


        for (PedidosEntity pedidos : pedidosEntityList) {
            boolean encontrado = false;
            for (PedidoAcumuladoDtoResponse pedidoAcumulado : resultados) {
                if (pedidoAcumulado.getCodigoInterno().equals(pedidos.getCodigoInterno())) {
                    pedidoAcumulado.setCantidad(pedidoAcumulado.getCantidad() + pedidos.getCantidad());
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                // No se encontró un pedido acumulado con el mismo código interno, crea uno nuevo
                PedidoAcumuladoDtoResponse nuevoPedidoAcumulado = new PedidoAcumuladoDtoResponse();
                nuevoPedidoAcumulado.setCodigoInterno(pedidos.getCodigoInterno());
                nuevoPedidoAcumulado.setId(pedidos.getId());
                nuevoPedidoAcumulado.setDni(pedidos.getDni());
                nuevoPedidoAcumulado.setNombreComercial(pedidos.getNombreComercial());
                nuevoPedidoAcumulado.setFormaDePago(pedidos.getFormaDePago());
                nuevoPedidoAcumulado.setStock(pedidos.getStock());
                nuevoPedidoAcumulado.setNumeroPedido(pedidos.getNumeroPedido());
                nuevoPedidoAcumulado.setValorTotal(pedidos.getValorTotalPedido());
                nuevoPedidoAcumulado.setTotalIva(pedidos.getIvaTotalPed());
                nuevoPedidoAcumulado.setNetoApagar(pedidos.getNetoApagar());
                nuevoPedidoAcumulado.setEstado(pedidos.getEstado());
                nuevoPedidoAcumulado.setCantidad(pedidos.getCantidad());
                nuevoPedidoAcumulado.setFechaCreación(pedidos.getFechaCreacion());

                resultados.add(nuevoPedidoAcumulado);
            }
        }

        for(PedidoAcumuladoDtoResponse  item : resultados){

            Double valorTotal = item.getValorTotal();
            int valorTotalInt = 0;

            if (valorTotal != null) {
                valorTotalInt = valorTotal.intValue();
            }


            if(valorTotalInt>= valor){
                consultaByTope.add(item);
            }

        }



        return consultaByTope;
    }
}

