package com.scb.externo.models.exceptions.email;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class ResourceInvalidEmailException extends RuntimeException{
    
    public ResourceInvalidEmailException(String mensagem){
        super(mensagem);
    }
}
