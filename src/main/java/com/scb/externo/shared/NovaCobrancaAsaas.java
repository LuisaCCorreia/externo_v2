package com.scb.externo.shared;

public class NovaCobrancaAsaas {
    private String customer;
    private static String billingType = "CREDIT_CARD";
    private String dueDate;
    private Float value;
    private String creditCardToken;

    public NovaCobrancaAsaas(String customer, String dueDate, Float value, String creditCardToken) {
        this.customer = customer;
        this.dueDate = dueDate;
        this.value = value;
        this.creditCardToken = creditCardToken;
    }

    public String getCustomer() {
        return customer;
    }
    public void setCustomer(String customer) {
        this.customer = customer;
    }
    public String getBillingType() {
        return billingType;
    }

    public String getDueDate() {
        return dueDate;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    public Float getValue() {
        return value;
    }
    public void setValue(Float value) {
        this.value = value;
    }
    public String getCreditCardToken() {
        return creditCardToken;
    }
    public void setCreditCardToken(String creditCardToken) {
        this.creditCardToken = creditCardToken;
    }

    
}
