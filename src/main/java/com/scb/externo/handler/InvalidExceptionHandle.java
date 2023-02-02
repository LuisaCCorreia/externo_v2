package com.scb.externo.handler;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.scb.externo.models.erros.ErroDadosInvalidos;
import com.scb.externo.models.exceptions.ResourceInvalidException;


@ControllerAdvice
public class InvalidExceptionHandle {

    @ExceptionHandler(ResourceInvalidException.class)
    public ResponseEntity<ErroDadosInvalidos> handleResourceInvalidEmailException(ResourceInvalidException exception) {
        ErroDadosInvalidos error = new ErroDadosInvalidos(UUID.randomUUID().toString(), HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
