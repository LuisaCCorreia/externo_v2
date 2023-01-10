package com.scb.externo.repository.email;

import org.springframework.stereotype.Repository;
import com.scb.externo.models.mongodb.DadosEmail;

import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface EmailRepository extends MongoRepository<DadosEmail, String>{}
