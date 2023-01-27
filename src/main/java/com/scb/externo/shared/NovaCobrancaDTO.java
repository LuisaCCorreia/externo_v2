package com.scb.externo.shared;

public class NovaCobrancaDTO {
    private float valor;
    private long ciclista;

    public NovaCobrancaDTO(float valor, long ciclista) {
        this.valor = valor;
        this.ciclista = ciclista;
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
}
