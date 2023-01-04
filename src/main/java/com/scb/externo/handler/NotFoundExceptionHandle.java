package com.scb.externo.handler;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.scb.externo.models.erros.NotFoundError;
import com.scb.externo.models.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class NotFoundExceptionHandle {

    private Random geradorId = new Random();

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<NotFoundError> handleResourceInvalidEmailException(ResourceNotFoundException exception) {
        NotFoundError error = new NotFoundError(Integer.toString(geradorId.nextInt(25)), HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
}
