package com.scb.externo.service.cartaocredito;

import java.io.IOException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.shared.NovaCobrancaDTO;
import com.scb.externo.shared.NovoCartaoDTO;

@Service
public class CartaoCreditoService {
  @Autowired
  AutenticarDadosService autenticarService;

  @Autowired
  CobrancaService cobrancaService;

  public ResponseEntity<String> autenticarCartao(NovoCartaoDTO novoCartao) throws IOException, InterruptedException, JSONException {
    return autenticarService.autenticarCartao(novoCartao);
  }

  public ResponseEntity<DadosCobranca> realizarCobranca(NovaCobrancaDTO novaCobranca) throws JSONException, IOException, InterruptedException {
    return cobrancaService.realizarCobranca( novaCobranca);
  } 

  public ResponseEntity<String> resgatarCobranca(String idCobranca) throws JSONException, IOException, InterruptedException{
   return cobrancaService.resgatarCobranca(idCobranca);
  }

  public ResponseEntity<DadosCobranca> colocarCobrancaFila(NovaCobrancaDTO novaCobranca) {
    return cobrancaService.colocarCobrancaFila(novaCobranca);
  }

  @Scheduled(fixedRate = 43200000)
  public ResponseEntity<String> processaCobrancasEmFila() throws IOException, InterruptedException, JSONException {
    return cobrancaService.processaCobrancasEmFila();
  }
}