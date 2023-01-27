package com.scb.externo.repository.cartaocredito;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.scb.externo.models.mongodb.DadosToken;

@Repository
public interface DadosCartaoRepository extends MongoRepository<DadosToken, String>{
    DadosToken findByCiclista(long ciclista);
}
