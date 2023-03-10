package com.scb.externo.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.scb.externo.models.exceptions.ResourceNotFoundException;
import com.scb.externo.models.mongodb.DadosEmail;
import com.scb.externo.repository.email.EmailRepository;
import com.scb.externo.shared.email.NovoEmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EnvioEmailService {
  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  EmailRepository emailRepository;
    
  public MimeMessage gerarEmail(NovoEmailDTO email) throws jakarta.mail.MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
   
    helper.setFrom("support@scb.com");
    helper.setSubject("Sistema de Controle de Bicicletário");
    helper.setText(email.getMensagem());
    helper.setTo(email.getEmail());
   
    return mimeMessage;
  }

  public ResponseEntity<DadosEmail> enviarEmail(NovoEmailDTO email) throws MessagingException {

    MimeMessage emailGerado = gerarEmail(email);
      
    try {

      mailSender.send(emailGerado);
      DadosEmail emailCriado = new DadosEmail();
      emailCriado.setEmail(email.getEmail());
      emailCriado.setMensagem(email.getMensagem());
      emailRepository.save(emailCriado);
          
      return new ResponseEntity<>(emailCriado, HttpStatus.OK);
    } catch(Exception e){
      throw new ResourceNotFoundException("E-mail não encontrado");
    }
  }
}
