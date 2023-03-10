package com.scb.externo.service.email;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.scb.externo.models.mongodb.DadosEmail;
import com.scb.externo.service.email.EmailService;
import com.scb.externo.shared.email.NovoEmailDTO;

@Service
public class EmailService {

  @Autowired
  EnvioEmailService envioService;

  public ResponseEntity<DadosEmail> enviarEmail(NovoEmailDTO novoEmail) throws MessagingException {
    return envioService.enviarEmail(novoEmail);
  }
}
