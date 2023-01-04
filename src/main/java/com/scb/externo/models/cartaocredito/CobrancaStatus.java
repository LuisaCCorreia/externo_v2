package com.scb.externo.models.cartaocredito;

public enum CobrancaStatus {
    PENDENTE, PAGA, FALHA, CANCELADA, OCUPADA;

    public String getStatus() {
        switch(this) {
            case PENDENTE:
                return "PENDENTE";
            case PAGA:
                return "PAGA";
            case FALHA:
                return "FALHA";
            case CANCELADA:
                return "CANCELADA";
            case OCUPADA:
                return "OCUPADA";
            default:
                return null;
        }
    }
}
