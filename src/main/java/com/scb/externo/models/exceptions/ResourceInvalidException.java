package com.scb.externo.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class ResourceInvalidException extends RuntimeException{

    public ResourceInvalidException(String mensagem){
        super(mensagem);
    }
}
