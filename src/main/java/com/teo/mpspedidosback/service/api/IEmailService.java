package com.teo.mpspedidosback.service.api;


import com.teo.mpspedidosback.entity.EmailEntity;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Session;


public interface IEmailService {

    void sendEmail(Message message);
    Message createEmail(Session session, EmailEntity emailEntity);


}
