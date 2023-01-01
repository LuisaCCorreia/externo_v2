package com.scb.externo.repository.cartaocredito;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.scb.externo.models.mongodb.DadosToken;

public interface CartaoRepositoryInterface extends MongoRepository<DadosToken, String>{
    DadosToken findByCiclista(String ciclista);
}
