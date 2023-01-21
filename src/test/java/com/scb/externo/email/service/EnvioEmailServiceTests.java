package com.scb.externo.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import com.scb.externo.service.email.EnvioEmailService;
import com.scb.externo.shared.email.NovoEmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@SpringBootTest
public class EnvioEmailServiceTests {

    @Spy
    JavaMailSender mockedMailSender;

    @InjectMocks
    EnvioEmailService envioService;
/*    
    @Test
    void enviar_email_sucesso() throws MessagingException {
        
        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("teste@email.com");
        novoEmail.setMensagem("Mensagem");

        envioService.enviarEmail(novoEmail);

        verify(mockedMailSender, times(1)).send(any(MimeMessage.class));

    }*/

    @Test
    void enviar_email_not_found() throws MessagingException {
        
        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("teste@email.com");
        novoEmail.setMensagem("Mensagem");
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        String mensagemRecebida="";
        String mensagemEsperada="E-mail não encontrado";
        
        when(mockedMailSender.createMimeMessage()).thenReturn( mailSenderImpl.createMimeMessage());
        doNothing().when(mockedMailSender).send(any(MimeMessage.class));

        try {
           envioService.enviarEmail(novoEmail); 
        } catch (Exception e) {
            mensagemRecebida = e.getMessage();
        }

        assertEquals(mensagemEsperada, mensagemRecebida);
    }
}
