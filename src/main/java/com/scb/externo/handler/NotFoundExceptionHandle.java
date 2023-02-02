package com.scb.externo.handler;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.scb.externo.models.erros.ErroDadosInvalidos;
import com.scb.externo.models.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class NotFoundExceptionHandle {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroDadosInvalidos> handleResourceInvalidEmailException(ResourceNotFoundException exception) {
        ErroDadosInvalidos error = new ErroDadosInvalidos(UUID.randomUUID().toString(), HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
}
