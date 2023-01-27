package com.scb.externo.shared;

public class NovoCartaoDTO {
    private long id;
    private String cvv;
    private String nomeTitular;
    private String numero;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    private String validade;

    public NovoCartaoDTO(long id, String cvv, String nomeTitular, String numero, String validade) {
        this.id = id;
        this.cvv = cvv;
        this.nomeTitular = nomeTitular;
        this.numero = numero;
        this.validade = validade;
    }
    
    public String getCvv() {
        return cvv;
    }
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    public String getNomeTitular() {
        return nomeTitular;
    }
    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getValidade() {
        return validade;
    }
    public void setValidade(String validade) {
        this.validade = validade;
    } 
}
