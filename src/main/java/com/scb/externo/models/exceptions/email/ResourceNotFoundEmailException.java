package com.scb.externo.models.exceptions.email;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ResourceNotFoundEmailException extends RuntimeException {

    public ResourceNotFoundEmailException(String mensagem){
        super(mensagem);
    }    
}
