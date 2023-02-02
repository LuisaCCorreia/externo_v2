package com.scb.externo.models.mongodb;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("dadosEmail")
public class DadosEmail {
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
}
