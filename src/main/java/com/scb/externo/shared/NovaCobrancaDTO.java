package com.scb.externo.shared;

public class NovaCobrancaDTO {
    private float valor;
    private String ciclista;

    public NovaCobrancaDTO(float valor, String ciclista) {
        this.valor = valor;
        this.ciclista = ciclista;
    }
    public float getValor() {
        return valor;
    }
    public void setValor(float valor) {
        this.valor = valor;
    }
    public String getCiclista() {
        return ciclista;
    }
    public void setCiclista(String ciclista) {
        this.ciclista = ciclista;
    }    
}
