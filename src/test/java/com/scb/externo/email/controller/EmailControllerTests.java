package com.scb.externo.email.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.scb.externo.controller.email.EmailController;
import com.scb.externo.models.email.Email;
import com.scb.externo.models.exceptions.ResourceInvalidException;
import com.scb.externo.models.exceptions.ResourceNotFoundException;
import com.scb.externo.service.email.EmailService;
import com.scb.externo.shared.email.NovoEmailDTO;
import jakarta.mail.MessagingException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EmailControllerTests {

    @Mock
    EmailService mockedemailService = mock(EmailService.class);

    @InjectMocks
    EmailController emailController = new EmailController();

    @Test
    void enviar_email_formato_invalido() throws ResourceInvalidException, MessagingException {
        
        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("luisa.c.correia@");
        novoEmail.setMensagem("Mensagem");

        String mensagemEsperada = "E-mail com formato inválido.";
        String mensagemRecebida = "";

        try {
            emailController.enviarEmail(novoEmail);    
        } catch (ResourceInvalidException e) {
            mensagemRecebida = e.getMessage();
        }         
        
        assertEquals(mensagemEsperada, mensagemRecebida);
    }

    @Test
    void enviar_email_invalido_email_nulo() throws ResourceInvalidException, MessagingException {
        
        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setMensagem("Mensagem");

        String mensagemEsperada = "E-mail com formato inválido.";
        String mensagemRecebida = "";

        try {
            emailController.enviarEmail(novoEmail);    
        } catch (ResourceInvalidException e) {
            mensagemRecebida = e.getMessage();
        }         
        
        assertEquals(mensagemEsperada, mensagemRecebida);
    }

    @Test
    void enviar_email_mensagem_nula() throws ResourceInvalidException, MessagingException {
        
        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("luisa.c.correia@edu.unirio.br");

        String mensagemEsperada = "E-mail com formato inválido.";
        String mensagemRecebida = "";

        try {
            emailController.enviarEmail(novoEmail);    
        } catch (ResourceInvalidException e) {
            mensagemRecebida = e.getMessage();
        }         
        
        assertEquals(mensagemEsperada, mensagemRecebida);
    }

    @Test
    void enviar_email_valido() throws MessagingException {
        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("luisa.c.correia@edu.unirio.br");
        novoEmail.setMensagem("Mensagem");

        Email emailCriado = new Email();
        emailCriado.setId(UUID.randomUUID().toString());
        emailCriado.setEmail(novoEmail.getEmail());
        emailCriado.setMensagem(novoEmail.getMensagem());
        
        when(mockedemailService.enviarEmail(novoEmail)).thenReturn(new ResponseEntity<Email>(emailCriado, HttpStatus.OK));
        ResponseEntity<Email> resposta = emailController.enviarEmail(novoEmail);
        
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    } 

    @Test
    void enviar_email_not_found_exception() throws MessagingException {
        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("luisa.c.correia@edu.unirio.br");
        novoEmail.setMensagem("Mensagem");
        
        when(mockedemailService.enviarEmail(novoEmail)).thenThrow(ResourceNotFoundException.class);
        assertThrows(
            ResourceNotFoundException.class, 
            () -> {
                emailController.enviarEmail(novoEmail);
            }
        );
    }   
}
