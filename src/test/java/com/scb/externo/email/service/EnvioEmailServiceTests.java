package com.scb.externo.email.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.scb.externo.models.email.Email;
import com.scb.externo.service.email.EnvioEmailService;
import com.scb.externo.shared.email.NovoEmailDTO;
import jakarta.mail.MessagingException;

@SpringBootTest
class EnvioEmailServiceTests {

    @Autowired
    EnvioEmailService envioService;
   
    @Test
    void enviar_email_sucesso() throws MessagingException {
        
        NovoEmailDTO novoEmail = new NovoEmailDTO();
        novoEmail.setEmail("teste@email.com");
        novoEmail.setMensagem("Mensagem");

        ResponseEntity<Email> responseEmail = envioService.enviarEmail(novoEmail);

        assertEquals(HttpStatus.OK,responseEmail.getStatusCode());

    }
}
