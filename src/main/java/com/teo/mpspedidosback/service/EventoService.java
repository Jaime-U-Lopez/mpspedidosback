package com.teo.mpspedidosback.service;


import com.teo.mpspedidosback.entity.EmailEntity;
import com.teo.mpspedidosback.entity.EventoEntity;
import com.teo.mpspedidosback.repository.IEmailRepository;
import com.teo.mpspedidosback.repository.IEventoRepository;
import com.teo.mpspedidosback.service.api.IEmailService;
import com.teo.mpspedidosback.service.api.IEventoService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Optional;
import java.util.Properties;


@Data
@Service
public class EventoService implements IEventoService {

    @Autowired
    private IEventoRepository eventoRepository;

    @Override
    public void createEvento(EventoEntity eventoEntity) {

        eventoRepository.save(eventoEntity);
    }

    @Override
    public EventoEntity getEvento(Long id) {
       Optional<EventoEntity> eventoEntity= eventoRepository.findById(id);

        return eventoEntity.get();
    }
}
