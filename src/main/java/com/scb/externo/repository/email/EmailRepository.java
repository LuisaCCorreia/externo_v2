package com.scb.externo.repository.email;

import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import com.scb.externo.models.email.Email;

@Repository
public class EmailRepository {
  private ArrayList<Email> memoria = new ArrayList<>();
  
  public void registrarEmail(Email email) {      
      memoria.add(email); 
	  }
}
