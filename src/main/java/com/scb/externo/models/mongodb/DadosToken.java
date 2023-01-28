package com.scb.externo.models.mongodb;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("dadosToken")
public class DadosToken {
   
    private long ciclista;
    private String customer;
    private String token;

    public String getCustomer() {
        return customer;
    }


    public void setCustomer(String customer) {
        this.customer = customer;
    }


    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }


    public long getCiclista() {
        return ciclista;
    }


    public void setCiclista(long ciclista) {
        this.ciclista = ciclista;
    }
    
}
