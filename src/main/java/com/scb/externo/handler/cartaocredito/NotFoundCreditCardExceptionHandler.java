package com.scb.externo.handler.cartaocredito;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.scb.externo.models.erros.NotFoundCreditCardError;
import com.scb.externo.models.exceptions.ResourceNotFoundCreditCardException;

@ControllerAdvice
public class NotFoundCreditCardExceptionHandler {
    Random geradorId = new Random();
    
    @ExceptionHandler(ResourceNotFoundCreditCardException.class)
    public ResponseEntity<NotFoundCreditCardError> handleResourceInvalidCreditCardException(ResourceNotFoundCreditCardException exception) {
        NotFoundCreditCardError error = new NotFoundCreditCardError(Integer.toString(geradorId.nextInt(25)), HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
