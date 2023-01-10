package com.scb.externo.service.cartaocredito;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.scb.externo.models.cartaocredito.CobrancaStatus;
import com.scb.externo.models.exceptions.ResourceNotFoundException;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.models.mongodb.DadosToken;
import com.scb.externo.repository.cartaocredito.CobrancaRepository;
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.shared.AsaasCobrancaResponseDTO;
import com.scb.externo.shared.NovaCobrancaAsaas;
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
    
    private NovaCobrancaAsaas gerarDadosCobranca(NovaCobrancaDTO novaCobranca) {
        
        String strDate = gerarDataAtual();  

        DadosToken dadosCartaoCiclista = cartaoRepository.findByCiclista(novaCobranca.getCiclista());

        return new NovaCobrancaAsaas(dadosCartaoCiclista.getCustomer(), strDate, novaCobranca.getValor(), dadosCartaoCiclista.getToken());
    }

    private void registrarDadosCobranca(DadosCobranca dadosCobranca ) {
        cobrancaRepository.save(dadosCobranca);
    }

    public ResponseEntity<DadosCobranca> realizarCobranca(MultiValueMap<String, String> headers, NovaCobrancaDTO novaCobranca) {
        String fazerCobrancaURL = "https://sandbox.asaas.com/api/v3/payments";
        
        NovaCobrancaAsaas novaCobrancaAsaas = gerarDadosCobranca(novaCobranca);

        HttpEntity<NovaCobrancaAsaas> entity = new HttpEntity<>(novaCobrancaAsaas, headers);

        AsaasCobrancaResponseDTO respostaCobranca = restTemplate.postForEntity(fazerCobrancaURL, entity, AsaasCobrancaResponseDTO.class).getBody();
        if(respostaCobranca != null) {

            DadosCobranca cobrancaResponse = new DadosCobranca();
            cobrancaResponse.setCiclista(novaCobranca.getCiclista());
            cobrancaResponse.setHoraFinalizacao(respostaCobranca.getDueDate());
            cobrancaResponse.setHoraSolicitacao(respostaCobranca.getDueDate());
            cobrancaResponse.setId(respostaCobranca.getId());
            cobrancaResponse.setStatus(CobrancaStatus.PAGA.getStatus());
            cobrancaResponse.setValor(respostaCobranca.getValue());

            registrarDadosCobranca(cobrancaResponse);

            return new ResponseEntity<>(cobrancaResponse, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Não encontrado");
        }
    }

    public ResponseEntity<AsaasCobrancaResponseDTO> resgatarCobranca(MultiValueMap<String, String> headers, String idCobranca) {
        String getCobrancaURL = "https://sandbox.asaas.com/api/v3/payments/{id}";

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
   
       return restTemplate.exchange(getCobrancaURL, HttpMethod.GET, request, AsaasCobrancaResponseDTO.class,idCobranca);
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
