package com.scb.externo.controller.email;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.scb.externo.models.email.Email;
import com.scb.externo.models.exceptions.ResourceInvalidException;
import com.scb.externo.service.email.EmailService;
import com.scb.externo.shared.email.NovoEmailDTO;

@RestController
@RequestMapping("/api/externo/email")
public class EmailController {

    @Autowired
	EmailService emailService;

	boolean validarEmail(String email) {
		if(email != null) {
			String[] partesEmail = email.split("@");
			return partesEmail.length == 2 && !partesEmail[0].equals("") && !partesEmail[1].equals("");
		} else {
			return false;
		}

	}

	boolean validarMensagem(String mensagem) {
		return mensagem != null;
	}

	@PostMapping("/enviarEmail")
	public ResponseEntity<Email> enviarEmail(@RequestBody NovoEmailDTO email) throws  MessagingException {

		if(!validarEmail(email.getEmail()) || !validarMensagem(email.getMensagem())) {
			throw new ResourceInvalidException("E-mail com formato inválido.");
		} else {			
			return emailService.enviarEmail(email);
		}	
	}
}
