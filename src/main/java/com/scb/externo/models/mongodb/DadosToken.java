package com.scb.externo.models.mongodb;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("dadosToken")
public class DadosToken {
   
    private String ciclista;
    private String customer;
    private String token;

    
    public DadosToken(String ciclista, String customer, String token) {
        this.ciclista = ciclista;
        this.customer = customer;
        this.token = token;
    }


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


    public String getCiclista() {
        return ciclista;
    }


    public void setCiclista(String ciclista) {
        this.ciclista = ciclista;
    }
    
}
