package com.scb.externo.shared;

public class NovaTokenizacao {
    private String customer;
    private CartaoCreditoAsaas creditCard;
    
    public NovaTokenizacao(String customer, CartaoCreditoAsaas creditCard) {
        this.customer = customer;
        this.creditCard = creditCard;
    }
    
    public String getCustomer() {
        return customer;
    }
    public void setCustomer(String customer) {
        this.customer = customer;
    }
    public CartaoCreditoAsaas getCreditCard() {
        return creditCard;
    }
    public void setCreditCard(CartaoCreditoAsaas creditCard) {
        this.creditCard = creditCard;
    }
}
