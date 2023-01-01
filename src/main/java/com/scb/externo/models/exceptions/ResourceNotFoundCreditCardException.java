package com.scb.externo.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundCreditCardException extends RuntimeException{
    
    public ResourceNotFoundCreditCardException(String mensagem){
        super(mensagem);
    }
}
