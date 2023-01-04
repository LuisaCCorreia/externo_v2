package com.scb.externo.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.scb.externo.models.email.Email;
import com.scb.externo.service.email.EmailService;
import com.scb.externo.service.email.EnvioEmailService;
import com.scb.externo.shared.email.NovoEmailDTO;

import jakarta.mail.MessagingException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmailServiceTests {

    @Mock
    EnvioEmailService mockedEnvioService;

    @InjectMocks
    EmailService emailService;


    @Test
    void enviar_email_service() throws MessagingException {

        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("luisa.c.correia@edu.unirio.br");
        novoEmail.setMensagem("Mensagem");

        Random geradorId = new Random();
        Email emailCriado = new Email();
        emailCriado.setId(Integer.toString(geradorId.nextInt(25)));
        emailCriado.setEmail(novoEmail.getEmail());
        emailCriado.setMensagem(novoEmail.getMensagem());

        when(mockedEnvioService.enviarEmail(novoEmail)).thenReturn(new ResponseEntity<Email>(emailCriado, HttpStatus.OK));
        ResponseEntity<Email> resposta = emailService.enviarEmail(novoEmail);
        
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
}
