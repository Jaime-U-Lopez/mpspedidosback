package com.teo.mpspedidosback.configuration;


import com.teo.mpspedidosback.exception.ExceptionGeneral;

import jakarta.persistence.NonUniqueResultException;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import javax.mail.internet.AddressException;
import javax.management.relation.RoleInfoNotFoundException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value={
                     RoleInfoNotFoundException.class,
            ExceptionGeneral.class,
            IllegalArgumentException.class,
            NullPointerException.class,
            IllegalStateException.class,
            SQLSyntaxErrorException.class,
            CommandAcceptanceException.class,
          //  MultipartException.class,
            NonUniqueResultException.class,
            IdentifierGenerationException.class,
            SQLException.class,
            NullPointerException.class,
            AddressException.class,
            IndexOutOfBoundsException.class,
    })

    public ResponseEntity<Object> BadRequestExceptionHandler(RuntimeException ex){
        ApiException apiException = new ApiException(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errorMessages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            } else {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        return new ResponseEntity<>(errorMessages, HttpStatus.BAD_REQUEST);
    }

}
