package com.scb.externo.handler;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.scb.externo.models.erros.InvalidDataError;
import com.scb.externo.models.exceptions.ResourceInvalidException;


@ControllerAdvice
public class InvalidExceptionHandle {
    private Random geradorId = new Random();

    @ExceptionHandler(ResourceInvalidException.class)
    public ResponseEntity<InvalidDataError> handleResourceInvalidEmailException(ResourceInvalidException exception) {
        InvalidDataError error = new InvalidDataError(Integer.toString(geradorId.nextInt(25)), HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
