package com.scb.externo.email;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.scb.externo.controller.email.EmailController;
import com.scb.externo.models.email.Email;
import com.scb.externo.models.exceptions.email.ResourceInvalidEmailException;
import com.scb.externo.service.email.EmailService;
import com.scb.externo.shared.email.NovoEmailDTO;

import jakarta.mail.MessagingException;


public class EmailControllerTests {
    EmailController emailController = new EmailController();

    EmailService emailService = mock(EmailService.class);

    @Test
    void enviar_email_valido() throws MessagingException {
        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("luisa.c.correia@edu.unirio.br");
        novoEmail.setMensagem("Mensagem");

        Random geradorId = new Random();
        Email emailCriado = new Email();
        emailCriado.setId(Integer.toString(geradorId.nextInt(25)));
        emailCriado.setEmail(novoEmail.getEmail());
        emailCriado.setMensagem(novoEmail.getMensagem());

        when(emailService.enviarEmail(novoEmail)).thenReturn(new ResponseEntity<Email>(emailCriado, HttpStatus.OK));
            ResponseEntity<Email> resposta = emailService.enviarEmail(novoEmail);
            assertEquals(HttpStatus.OK, resposta.getStatusCode());
    } 
    
}
