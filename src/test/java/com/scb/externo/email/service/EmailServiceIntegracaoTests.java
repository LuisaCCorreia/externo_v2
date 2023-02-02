package com.scb.externo.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.scb.externo.models.mongodb.DadosEmail;
import com.scb.externo.service.email.EmailService;
import com.scb.externo.shared.email.NovoEmailDTO;
import jakarta.mail.MessagingException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EmailServiceIntegracaoTests {

    @Autowired
    EmailService emailService;

    @Test
    void enviar_email__valido() throws MessagingException {

        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("luisa.c.correia@edu.unirio.br");
        novoEmail.setMensagem("Mensagem");

        ResponseEntity<DadosEmail> resposta = emailService.enviarEmail(novoEmail);
        
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
}
