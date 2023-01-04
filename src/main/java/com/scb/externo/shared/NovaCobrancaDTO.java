package com.scb.externo.shared;

public class NovaCobrancaDTO {
    private Float valor;
    private String ciclista;

    public NovaCobrancaDTO(Float valor, String ciclista) {
        this.valor = valor;
        this.ciclista = ciclista;
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
