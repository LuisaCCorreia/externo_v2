package com.scb.externo.models.email;

public class Email {

    private String id;
    private String emailCadastrado;
    private String mensagem;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getEmail() {
        return emailCadastrado;
    }

    public void setEmail(String email) {
        this.emailCadastrado = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
