package com.scb.externo.models.mongodb;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("dadosCobranca")
public class DadosCobranca {
    private String id;
    private String status;
    private String horaSolicitacao;
    private String horaFinalizacao;
    private Float valor;
    private String ciclista;

    public DadosCobranca(String id, String status, String horaSolicitacao, String horaFinalizacao, Float valor, String ciclista) {
        this.id = id;
        this.status = status;
        this.horaSolicitacao = horaSolicitacao;
        this.horaFinalizacao = horaFinalizacao;
        this.valor = valor;
        this.ciclista = ciclista;
    }

    public String getId() {
        return id;
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
    public Float getValor() {
        return valor;
    }
    public void setValor(Float valor) {
        this.valor = valor;
    }
    public String getCiclista() {
        return ciclista;
    }
    public void setCiclista(String ciclista) {
        this.ciclista = ciclista;
    }
}
