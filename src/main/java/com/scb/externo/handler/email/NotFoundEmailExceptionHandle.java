package com.scb.externo.handler.email;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.scb.externo.models.erros.email.NotFoundEmailError;
import com.scb.externo.models.exceptions.email.ResourceNotFoundEmailException;

@ControllerAdvice
public class NotFoundEmailExceptionHandle {

    private Random geradorId = new Random();

    @ExceptionHandler(ResourceNotFoundEmailException.class)
    public ResponseEntity<NotFoundEmailError> handleResourceInvalidEmailException(ResourceNotFoundEmailException exception) {
        NotFoundEmailError error = new NotFoundEmailError(Integer.toString(geradorId.nextInt(25)), HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
}
