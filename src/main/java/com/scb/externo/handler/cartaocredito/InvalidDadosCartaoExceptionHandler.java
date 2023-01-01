package com.scb.externo.handler.cartaocredito;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.scb.externo.models.erros.InvalidCreditCardDataError;
import com.scb.externo.models.exceptions.ResourceInvalidCreditCardDataException;

@ControllerAdvice
public class InvalidDadosCartaoExceptionHandler {
    private Random geradorId = new Random();

    @ExceptionHandler(ResourceInvalidCreditCardDataException.class)
    public ResponseEntity<InvalidCreditCardDataError> handleResourceInvalidCreditCardException(ResourceInvalidCreditCardDataException exception) {
        InvalidCreditCardDataError error = new InvalidCreditCardDataError(Integer.toString(geradorId.nextInt(25)), HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
