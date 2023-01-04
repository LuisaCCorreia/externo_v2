package com.scb.externo.service.cartaocredito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.shared.APICartaoTokenResponse;
import com.scb.externo.shared.NovaCobrancaDTO;
import com.scb.externo.shared.NovoCartaoDTO;

@Service
public class CartaoCreditoService {
   @Autowired
   AutenticarDadosService autenticarService;

   @Autowired
   CobrancaService cobrancaService;

   public ResponseEntity<APICartaoTokenResponse> autenticarCartao(MultiValueMap<String, String> headers, NovoCartaoDTO novoCartao) {
     return autenticarService.autenticarCartao(headers, novoCartao);
   }

   public ResponseEntity<DadosCobranca> realizarCobranca(MultiValueMap<String, String> headers, NovaCobrancaDTO novaCobranca) {
     return cobrancaService.realizarCobranca(headers, novaCobranca);
   } 
}
