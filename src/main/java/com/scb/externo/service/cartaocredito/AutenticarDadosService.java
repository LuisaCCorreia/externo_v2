package com.scb.externo.service.cartaocredito;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.scb.externo.models.exceptions.ResourceNotFoundException;
import com.scb.externo.models.mongodb.DadosToken;
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.shared.NovoCartaoDTO;

@Service
public class AutenticarDadosService {

  @Autowired
  DadosCartaoRepository cartaoRepository;

  @Autowired
  RestTemplate restTemplate;

  private String codigoUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

  private void registrarDadosAutenticacao(String clienteId, String tokenCartao) {
    DadosToken dadosToken = new DadosToken(codigoUUID(),clienteId, tokenCartao);
    cartaoRepository.save(dadosToken);
  }

  public ResponseEntity<JSONObject> criarCliente(MultiValueMap<String, String> headers, String nomeTitular) throws IOException, InterruptedException, JSONException {
    String criarClienteURL = "https://sandbox.asaas.com/api/v3/customers";
    HttpRequest httpRequest = HttpRequest.newBuilder()
    .POST(BodyPublishers.ofString("{\"name\": \"" + nomeTitular+"\"}")).
    uri(URI.create(criarClienteURL))
    .headers("Content-Type", "application/json")
    .headers("access_token", "$aact_YTU5YTE0M2M2N2I4MTliNzk0YTI5N2U5MzdjNWZmNDQ6OjAwMDAwMDAwMDAwMDAwNDU1NDA6OiRhYWNoXzcxM2I0ODFhLTM3M2QtNGM3Ny04MWNiLTdkY2U5YzE0OWNkOA==")
    .build();

    HttpClient client = HttpClient.newBuilder().build();
    HttpResponse<String> res = client.send(httpRequest, BodyHandlers.ofString());
   JSONObject teste = new JSONObject(res.body());

    return new ResponseEntity<>(teste, HttpStatus.OK);
  }

  public ResponseEntity<String> autenticarCartao(MultiValueMap<String, String> headers, NovoCartaoDTO novoCartao) throws IOException, InterruptedException, JSONException {
    JSONObject novoCliente = criarCliente(headers, novoCartao.getNomeTitular()).getBody();

    if(novoCliente != null) {
      LocalDate data = LocalDate.parse(novoCartao.getValidade());

      String teste2 = "{\"creditCard\": {\"holderName\":\"" 
      + novoCliente.get("name").toString()
      +"\", \"number\":\"" + novoCartao.getNumero() 
      + "\", \"expiryMonth\":\"" + Integer.toString(data.getMonthValue())
      +"\", \"expiryYear\":\""+Integer.toString(data.getYear()) 
      + "\", \"ccv\":\"" + novoCartao.getCvv() + "\"}, \"customer\":\"" 
      + novoCliente.get("id").toString() + "\"}";
      String autenticarCartaoURL = "https://sandbox.asaas.com/api/v3/creditCard/tokenize";
      HttpRequest httpRequest = HttpRequest.newBuilder()
      .POST(BodyPublishers.ofString(teste2)).
      uri(URI.create(autenticarCartaoURL))
      .headers("Content-Type", "application/json")
      .headers("access_token", "$aact_YTU5YTE0M2M2N2I4MTliNzk0YTI5N2U5MzdjNWZmNDQ6OjAwMDAwMDAwMDAwMDAwNDU1NDA6OiRhYWNoXzcxM2I0ODFhLTM3M2QtNGM3Ny04MWNiLTdkY2U5YzE0OWNkOA==")
      .build();
  
      HttpClient client = HttpClient.newBuilder().build();
      HttpResponse<String> res = client.send(httpRequest, BodyHandlers.ofString());
      JSONObject teste3 = new JSONObject(res.body());

      registrarDadosAutenticacao(novoCliente.get("id").toString(), teste3.get("creditCardToken").toString());         

      return new ResponseEntity<>(teste3.toString(), HttpStatus.OK);
    }   
    
    throw new ResourceNotFoundException("Não encontrado.");  
  }
}
