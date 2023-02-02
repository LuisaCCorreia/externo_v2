package com.scb.externo.service.cartaocredito;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.scb.externo.consts.HeaderConsts;
import com.scb.externo.models.exceptions.ResourceNotFoundException;
import com.scb.externo.models.mongodb.DadosToken;
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.shared.NovoCartaoDTO;

@Service
public class AutenticarDadosService {

  @Autowired
  DadosCartaoRepository cartaoRepository;

  private void registrarDadosAutenticacao(long idCiclista,String clienteId, String tokenCartao) {
    DadosToken dadosToken = new DadosToken();
    dadosToken.setCiclista(idCiclista);
    dadosToken.setCustomer(clienteId);
    dadosToken.setToken(tokenCartao);

    cartaoRepository.save(dadosToken);
  }

  private void AtualizarDadosAutenticacao(DadosToken dadosToken, String novoToken) {
    
    dadosToken.setToken(novoToken);

    cartaoRepository.save(dadosToken);
  }

  public ResponseEntity<JSONObject> criarCliente( String nomeTitular) throws IOException, InterruptedException, JSONException {
    String criarClienteURL = "https://sandbox.asaas.com/api/v3/customers";
    HttpRequest httpRequest = HttpRequest.newBuilder()
    .POST(BodyPublishers.ofString("{\"name\": \"" + nomeTitular+"\"}")).
    uri(URI.create(criarClienteURL))
    .headers(HeaderConsts.CONTENT, HeaderConsts.APPLICATION)
    .headers(HeaderConsts.ACCESS_TOKEN, HeaderConsts.ASAASKEY)
    .build();

    HttpClient client = HttpClient.newBuilder().build();
    HttpResponse<String> res = client.send(httpRequest, BodyHandlers.ofString());
    JSONObject responseCriarCliente = new JSONObject(res.body());

    return new ResponseEntity<>(responseCriarCliente, HttpStatus.OK);
  }

  public ResponseEntity<String> autenticarCartao(NovoCartaoDTO novoCartao) throws IOException, InterruptedException, JSONException {
    
    String idCliente = "";

    DadosToken clienteExistente = cartaoRepository.findByCiclista(novoCartao.getId());
    
    if(clienteExistente != null) {
      idCliente = clienteExistente.getCustomer();
      
    } else {
      JSONObject novoCliente = criarCliente(novoCartao.getNomeTitular()).getBody();

      if(novoCliente != null) {
      idCliente = novoCliente.get("id").toString();
      }
    }
    

    if(!idCliente.equals("")) {
      LocalDate data = LocalDate.parse(novoCartao.getValidade());

      String bodyAutenticarCartao = "{\"creditCard\": {\"holderName\":\"" 
      + novoCartao.getNomeTitular()
      +"\", \"number\":\"" + novoCartao.getNumero() 
      + "\", \"expiryMonth\":\"" + Integer.toString(data.getMonthValue())
      +"\", \"expiryYear\":\""+Integer.toString(data.getYear()) 
      + "\", \"ccv\":\"" + novoCartao.getCvv() + "\"}, \"customer\":\"" 
      + idCliente + "\"}";
      String autenticarCartaoURL = "https://sandbox.asaas.com/api/v3/creditCard/tokenize";
      HttpRequest httpRequest = HttpRequest.newBuilder()
      .POST(BodyPublishers.ofString(bodyAutenticarCartao)).
      uri(URI.create(autenticarCartaoURL))
      .headers(HeaderConsts.CONTENT, HeaderConsts.APPLICATION)
      .headers(HeaderConsts.ACCESS_TOKEN, HeaderConsts.ASAASKEY)
      .build();
  
      HttpClient client = HttpClient.newBuilder().build();
      JSONObject responseTokenizacao = new JSONObject(client.send(httpRequest, BodyHandlers.ofString()).body());

      if(clienteExistente != null) {
       AtualizarDadosAutenticacao(clienteExistente, responseTokenizacao.get("creditCardToken").toString());
      } else {
        registrarDadosAutenticacao(novoCartao.getId(), idCliente, responseTokenizacao.get("creditCardToken").toString());         
      }
      
      return new ResponseEntity<>("Dados Atualizados", HttpStatus.OK);
    }   
    
    throw new ResourceNotFoundException("Não encontrado.");  
  }
}
