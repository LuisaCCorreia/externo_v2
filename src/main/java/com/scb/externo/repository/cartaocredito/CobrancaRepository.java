package com.scb.externo.repository.cartaocredito;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.scb.externo.models.mongodb.DadosCobranca;

public interface CobrancaRepository extends MongoRepository<DadosCobranca, String>{
    
}
