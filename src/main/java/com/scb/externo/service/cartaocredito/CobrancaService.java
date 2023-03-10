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
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.scb.externo.consts.HeaderConsts;
import com.scb.externo.models.cartaocredito.CobrancaStatus;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.models.mongodb.DadosToken;
import com.scb.externo.repository.cartaocredito.CobrancaRepository;
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.shared.NovaCobrancaDTO;

@Service
public class CobrancaService {

    @Autowired
    DadosCartaoRepository cartaoRepository;

    @Autowired
    CobrancaRepository cobrancaRepository;

    String fazerCobrancaURL = "https://sandbox.asaas.com/api/v3/payments";

    private String gerarDataAtual() {
          
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");  
        return dateFormat.format(date);  
    }
    
    private String gerarDadosCobranca(String customer,float valor, String token) {
        
        String strDate = gerarDataAtual();  

        return "{\"customer\":\"" + customer + "\", \"billingType\":\"CREDIT_CARD\","+
        "\"dueDate\":\"" + strDate + "\", \"value\":\"" + valor + "\", \"creditCardToken\":\"" 
        + token + "\"}";
    }

    private void registrarDadosCobranca(DadosCobranca dadosCobranca ) {
        cobrancaRepository.save(dadosCobranca);
    }

    public ResponseEntity<DadosCobranca> realizarCobranca(NovaCobrancaDTO novaCobranca) throws JSONException, IOException, InterruptedException {

        DadosToken dadosCartaoCiclista = cartaoRepository.findByCiclista(novaCobranca.getCiclista());
        
        String bodyRealizarCobranca = gerarDadosCobranca( dadosCartaoCiclista.getCustomer(),
        novaCobranca.getValor(), dadosCartaoCiclista.getToken());

        HttpRequest httpRequest = HttpRequest.newBuilder()
        .POST(BodyPublishers.ofString(bodyRealizarCobranca)).
        uri(URI.create(fazerCobrancaURL))
        .headers(HeaderConsts.CONTENT, HeaderConsts.APPLICATION)
        .headers(HeaderConsts.ACCESS_TOKEN, HeaderConsts.ASAASKEY)
        .build();
    
        HttpClient client = HttpClient.newBuilder().build();
        JSONObject responseRealizarCobranca = new JSONObject(client.send(httpRequest, BodyHandlers.ofString()).body()) ;

        int valorCobrado = (int) responseRealizarCobranca.get("value");

        DadosCobranca cobrancaResponse = new DadosCobranca();
        cobrancaResponse.setCiclista(novaCobranca.getCiclista());
        cobrancaResponse.setHoraFinalizacao(responseRealizarCobranca.get("dueDate").toString());
        cobrancaResponse.setHoraSolicitacao(responseRealizarCobranca.get("dueDate").toString());
        cobrancaResponse.setCobrancaId(responseRealizarCobranca.get("id").toString());
        cobrancaResponse.setStatus(CobrancaStatus.PAGA.getStatus());
        cobrancaResponse.setValor(valorCobrado);
        cobrancaResponse.setCustomer(responseRealizarCobranca.get("customer").toString());
        cobrancaResponse.setToken(dadosCartaoCiclista.getToken());

        registrarDadosCobranca(cobrancaResponse);

        return new ResponseEntity<>(cobrancaResponse, HttpStatus.OK);
       
    }

    public ResponseEntity<String> resgatarCobranca(String idCobranca) throws IOException, InterruptedException {
        String getCobrancaURL = "https://sandbox.asaas.com/api/v3/payments/" + idCobranca;

        HttpRequest httpRequest = HttpRequest.newBuilder()
        .GET()
        .uri(URI.create(getCobrancaURL))
        .headers(HeaderConsts.CONTENT, HeaderConsts.APPLICATION)
        .headers(HeaderConsts.ACCESS_TOKEN, HeaderConsts.ASAASKEY)
        .build();
    
        HttpClient client = HttpClient.newBuilder().build();
        String resgatarCobranca = client.send(httpRequest, BodyHandlers.ofString()).body();
   
       return new ResponseEntity<>(resgatarCobranca, HttpStatus.OK);
    }


    public ResponseEntity<DadosCobranca> colocarCobrancaFila(NovaCobrancaDTO novaCobranca) {

        String dataAtual = gerarDataAtual();
        DadosToken dadosCartaoCiclista = cartaoRepository.findByCiclista(novaCobranca.getCiclista());
        
        DadosCobranca dadosCobrancaFila = new DadosCobranca();
        dadosCobrancaFila.setCiclista(novaCobranca.getCiclista());
        dadosCobrancaFila.setHoraSolicitacao(dataAtual);
        dadosCobrancaFila.setHoraFinalizacao(dataAtual);
        dadosCobrancaFila.setStatus(CobrancaStatus.PENDENTE.getStatus());
        dadosCobrancaFila.setValor(novaCobranca.getValor());
        dadosCobrancaFila.setCobrancaId("");
        dadosCobrancaFila.setCustomer(dadosCartaoCiclista.getCustomer());
        dadosCobrancaFila.setToken(dadosCartaoCiclista.getToken());

        registrarDadosCobranca(dadosCobrancaFila);
        return new ResponseEntity<>(dadosCobrancaFila, HttpStatus.OK);
    }

    @Scheduled(fixedRate = 43200000)
    public ResponseEntity<String> processaCobrancasEmFila() throws IOException, InterruptedException, JSONException {        List<DadosCobranca> cobrancasPendentes = cobrancaRepository.findByStatus(CobrancaStatus.PENDENTE.getStatus());

        if(!cobrancasPendentes.isEmpty()) {
            for(int i = 0; i < cobrancasPendentes.size(); i++) {
                String cobrancaAtrasada = gerarDadosCobranca(cobrancasPendentes.get(i).getCustomer(), 
                cobrancasPendentes.get(i).getValor(), 
                cobrancasPendentes.get(i).getToken());
                HttpRequest httpRequest = HttpRequest.newBuilder()
                .POST(BodyPublishers.ofString(cobrancaAtrasada)).
                uri(URI.create(fazerCobrancaURL))
                .headers(HeaderConsts.CONTENT, HeaderConsts.APPLICATION)
                .headers(HeaderConsts.ACCESS_TOKEN, HeaderConsts.ASAASKEY)
                .build();
                
                HttpClient client = HttpClient.newBuilder().build();
                JSONObject cobrancaRealizada = new JSONObject(client.send(httpRequest, BodyHandlers.ofString()).body()) ;
                cobrancasPendentes.get(i).setCobrancaId(cobrancaRealizada.get("id").toString());
                cobrancasPendentes.get(i).setStatus(CobrancaStatus.PAGA.getStatus());
                cobrancaRepository.save(cobrancasPendentes.get(i));
            }
        }
        
        return new ResponseEntity<>("Processamento conclu??do com sucesso", HttpStatus.OK);
        } 
     
    } 

