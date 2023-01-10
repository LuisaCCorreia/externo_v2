package com.scb.externo.models.mongodb;

import org.springframework.data.mongodb.core.mapping.Document;

import com.scb.externo.models.email.Email;

@Document("dadosEmail")
public class DadosEmail {
    private Email email;
    public Email getEmail() {
        return email;
    }
    public void setEmail(Email email) {
        this.email = email;
    }  
}
