package com.scb.externo.email.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.scb.externo.controller.email.EmailController;
import com.scb.externo.models.mongodb.DadosEmail;
import com.scb.externo.shared.email.NovoEmailDTO;
import jakarta.mail.MessagingException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EmailControllerIntegracaoTests {

    @Autowired
    EmailController emailController;

    @Test
    void enviar_email_valido() throws MessagingException {
        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("luisa.c.correia@edu.unirio.br");
        novoEmail.setMensagem("Mensagem");

        ResponseEntity<DadosEmail> resposta = emailController.enviarEmail(novoEmail);
        
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }  
}
