package com.scb.externo.models.cartaocredito;

public enum CobrancaStatus {
    PENDENTE, PAGA;

    public String getStatus() {
        switch(this) {
            case PENDENTE:
                return "PENDENTE";
            case PAGA:
                return "PAGA";
            default:
                return null;
        }
    }
}
