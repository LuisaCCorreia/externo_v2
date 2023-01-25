package com.scb.externo.service.cartaocredito;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.scb.externo.models.cartaocredito.CobrancaStatus;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.models.mongodb.DadosToken;
import com.scb.externo.repository.cartaocredito.CobrancaRepository;
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.shared.NovaCobrancaDTO;

@Service
public class CobrancaService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DadosCartaoRepository cartaoRepository;

    @Autowired
    CobrancaRepository cobrancaRepository;

    private String gerarDataAtual() {
          
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");  
        return dateFormat.format(date);  
    }
    
    private String gerarDadosCobranca(NovaCobrancaDTO novaCobranca) {
        
        String strDate = gerarDataAtual();  

        DadosToken dadosCartaoCiclista = cartaoRepository.findByCiclista(novaCobranca.getCiclista());
        return "{\"customer\":\"" + dadosCartaoCiclista.getCustomer() + "\", \"billingType\":\"CREDIT_CARD\","+
        "\"dueDate\":\"" + strDate + "\", \"value\":\"" + novaCobranca.getValor() + "\", \"creditCardToken\":\"" 
        + dadosCartaoCiclista.getToken() + "\"}";
    }

    private void registrarDadosCobranca(DadosCobranca dadosCobranca ) {
        cobrancaRepository.save(dadosCobranca);
    }

    public ResponseEntity<DadosCobranca> realizarCobranca(NovaCobrancaDTO novaCobranca) throws JSONException, IOException, InterruptedException {
        String fazerCobrancaURL = "https://sandbox.asaas.com/api/v3/payments";
        
        String bodyRealizarCobranca = gerarDadosCobranca(novaCobranca);

        HttpRequest httpRequest = HttpRequest.newBuilder()
        .POST(BodyPublishers.ofString(bodyRealizarCobranca)).
        uri(URI.create(fazerCobrancaURL))
        .headers("Content-Type", "application/json")
        .headers("access_token", "$aact_YTU5YTE0M2M2N2I4MTliNzk0YTI5N2U5MzdjNWZmNDQ6OjAwMDAwMDAwMDAwMDAwNDU1NDA6OiRhYWNoXzcxM2I0ODFhLTM3M2QtNGM3Ny04MWNiLTdkY2U5YzE0OWNkOA==")
        .build();
    
        HttpClient client = HttpClient.newBuilder().build();
        JSONObject responseRealizarCobranca = new JSONObject(client.send(httpRequest, BodyHandlers.ofString()).body()) ;

        int valorCobrado = (int) responseRealizarCobranca.get("value");
        

        DadosCobranca cobrancaResponse = new DadosCobranca();
        cobrancaResponse.setCiclista(novaCobranca.getCiclista());
        cobrancaResponse.setHoraFinalizacao(responseRealizarCobranca.get("dueDate").toString());
        cobrancaResponse.setHoraSolicitacao(responseRealizarCobranca.get("dueDate").toString());
        cobrancaResponse.setId(responseRealizarCobranca.get("id").toString());
        cobrancaResponse.setStatus(CobrancaStatus.PAGA.getStatus());
        cobrancaResponse.setValor(valorCobrado);

        registrarDadosCobranca(cobrancaResponse);

        return new ResponseEntity<>(cobrancaResponse, HttpStatus.OK);
       
    }

    public ResponseEntity<String> resgatarCobranca(String idCobranca) throws JSONException, IOException, InterruptedException {
        String getCobrancaURL = "https://sandbox.asaas.com/api/v3/payments/" + idCobranca;

        HttpRequest httpRequest = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(getCobrancaURL))
        .headers("Content-Type", "application/json")
        .headers("access_token", "$aact_YTU5YTE0M2M2N2I4MTliNzk0YTI5N2U5MzdjNWZmNDQ6OjAwMDAwMDAwMDAwMDAwNDU1NDA6OiRhYWNoXzcxM2I0ODFhLTM3M2QtNGM3Ny04MWNiLTdkY2U5YzE0OWNkOA==")
        .build();
    
        HttpClient client = HttpClient.newBuilder().build();
        String resgatarCobranca = client.send(httpRequest, BodyHandlers.ofString()).body();
   
       return new ResponseEntity<>(resgatarCobranca, HttpStatus.OK);
    }


    public ResponseEntity<DadosCobranca> colocarCobrancaFila(NovaCobrancaDTO novaCobranca) {

        String dataAtual = gerarDataAtual();
        
        DadosCobranca dadosCobrancaFila = new DadosCobranca();
        dadosCobrancaFila.setCiclista(novaCobranca.getCiclista());
        dadosCobrancaFila.setHoraSolicitacao(dataAtual);
        dadosCobrancaFila.setHoraFinalizacao(dataAtual);
        dadosCobrancaFila.setStatus(CobrancaStatus.PENDENTE.getStatus());
        dadosCobrancaFila.setValor(novaCobranca.getValor());

        registrarDadosCobranca(dadosCobrancaFila);
        return new ResponseEntity<>(dadosCobrancaFila, HttpStatus.OK);
      
    }
    
}
