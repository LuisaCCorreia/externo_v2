package com.scb.externo.repository.cartaocredito;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.scb.externo.models.mongodb.DadosCobranca;

@Repository
public interface CobrancaRepository extends MongoRepository<DadosCobranca, String>{
    List<DadosCobranca> findByStatus(String status);
}
