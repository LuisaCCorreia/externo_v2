package com.scb.externo.models.mongodb;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("dadosCobranca")
public class DadosCobranca {
    private String id;
    private String status;
    private String horaSolicitacao;
    private String horaFinalizacao;
    private float valor;
    private long ciclista;
    private String customer;
    private String token;

    public String getId() {
        return id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getHoraSolicitacao() {
        return horaSolicitacao;
    }
    public void setHoraSolicitacao(String horaSolicitacao) {
        this.horaSolicitacao = horaSolicitacao;
    }
    public String getHoraFinalizacao() {
        return horaFinalizacao;
    }
    public void setHoraFinalizacao(String horaFinalizacao) {
        this.horaFinalizacao = horaFinalizacao;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
    public long getCiclista() {
        return ciclista;
    }
    public void setCiclista(long ciclista) {
        this.ciclista = ciclista;
    }
    public String getCustomer() {
        return customer;
    }
    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
