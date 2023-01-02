package com.scb.externo.service.cartaocredito;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.scb.externo.models.exceptions.ResourceNotFoundCreditCardException;
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
    DadosCartaoRepository cartaoRepository;

    @Autowired
    CobrancaRepository cobrancaRepository;
    
    public ResponseEntity<DadosCobranca> realizarCobranca(MultiValueMap<String, String> headers, NovaCobrancaDTO novaCobranca) {
        String fazerCobrancaURL = "https://sandbox.asaas.com/api/v3/payments";
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(date);  

        DadosToken dadosCartaoCiclista = cartaoRepository.findByCiclista(novaCobranca.getCiclista());

        NovaCobrancaAsaas novaCobrancaAsaas = new NovaCobrancaAsaas();

        novaCobrancaAsaas.setCustomer(dadosCartaoCiclista.getCustomer());
        novaCobrancaAsaas.setDueDate(strDate);
        novaCobrancaAsaas.setValue(novaCobranca.getValor());
        novaCobrancaAsaas.setCreditCardToken(dadosCartaoCiclista.getToken());
        
        HttpEntity<NovaCobrancaAsaas> entity = new HttpEntity<>(novaCobrancaAsaas, headers);

        AsaasCobrancaResponseDTO respostaCobranca = new RestTemplate().postForEntity(fazerCobrancaURL, entity, AsaasCobrancaResponseDTO.class).getBody();
        
        if(respostaCobranca != null) {
            DadosCobranca cobrancaResponse = new DadosCobranca(respostaCobranca.getId(), respostaCobranca.getStatus(), strDate, strDate, respostaCobranca.getValue(), novaCobranca.getCiclista());

            cobrancaRepository.save(cobrancaResponse);
            return new ResponseEntity<>(cobrancaResponse, headers, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundCreditCardException("Não encontrado");
        }

    }
}
