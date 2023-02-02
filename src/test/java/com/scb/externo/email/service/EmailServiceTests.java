package com.scb.externo.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.scb.externo.models.exceptions.ResourceNotFoundException;
import com.scb.externo.models.mongodb.DadosEmail;
import com.scb.externo.service.email.EmailService;
import com.scb.externo.service.email.EnvioEmailService;
import com.scb.externo.shared.email.NovoEmailDTO;
import jakarta.mail.MessagingException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EmailServiceTests {

    @Mock
    EnvioEmailService mockedEnvioService;

    @InjectMocks
    EmailService emailService;


    @Test
    void enviar_email__valido() throws MessagingException {

        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("luisa.c.correia@edu.unirio.br");
        novoEmail.setMensagem("Mensagem");

        DadosEmail emailCriado = new DadosEmail();
        emailCriado.setEmail(novoEmail.getEmail());
        emailCriado.setMensagem(novoEmail.getMensagem());

        when(mockedEnvioService.enviarEmail(novoEmail)).thenReturn(new ResponseEntity<>(emailCriado, HttpStatus.OK));
        ResponseEntity<DadosEmail> resposta = emailService.enviarEmail(novoEmail);
        
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    
    @Test
    void enviar_email__not_found_exception() throws MessagingException {

        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("luisa.c.correia@edu.unirio.br");
        novoEmail.setMensagem("Mensagem");

        when(mockedEnvioService.enviarEmail(novoEmail)).thenThrow(ResourceNotFoundException.class);
        assertThrows(
            ResourceNotFoundException.class, 
            ()->{
                emailService.enviarEmail(novoEmail);
            }
        );
    }
}
