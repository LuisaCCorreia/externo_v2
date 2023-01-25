package com.scb.externo.cartaocredito.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.scb.externo.consts.Key;
import com.scb.externo.models.exceptions.ResourceNotFoundException;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.models.mongodb.DadosToken;
import com.scb.externo.repository.cartaocredito.CobrancaRepository;
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.service.cartaocredito.CobrancaService;
import com.scb.externo.shared.APICartaoTokenResponse;
import com.scb.externo.shared.AsaasCobrancaResponseDTO;
import com.scb.externo.shared.NovaCobrancaDTO;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CobrancaServiceTests {

    @Mock
    RestTemplate mockedRestTemplate; 

    @Mock
    DadosCartaoRepository mockedCartaoRepository;

    @Mock
    CobrancaRepository mockedCobrancaRespository;

    @InjectMocks
    CobrancaService cobrancaService;

    //Testes realizar cobrança
    @Test
    void realizar_cobranca_valida() throws JSONException, IOException, InterruptedException {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 10.00 , "7b7476c7-60a7-46a3-b7fe-45d28eb18e99");
        DadosToken dadosToken = new DadosToken(novaCobranca.getCiclista(), "cus_000005077278", "4ae91611-5a92-42bf-ad17-a45124c11b19");

        APICartaoTokenResponse cartaoCredito = new APICartaoTokenResponse();
        cartaoCredito.setCreditCardBrand("MASTERCARD");
        cartaoCredito.setCreditCardNumber("8829");
        cartaoCredito.setCreditCardToken("c8594e1d-18bd-4520-af74-be2a45820b41");

        AsaasCobrancaResponseDTO respostaCobrancaBody = new AsaasCobrancaResponseDTO();
        respostaCobrancaBody.setObject("payment");
        respostaCobrancaBody.setId("pay_4563534755995298");
        respostaCobrancaBody.setDateCreated("2023-01-08");
        respostaCobrancaBody.setCustomer("cus_000005075166");
        respostaCobrancaBody.setValue((float) 10.00);
        respostaCobrancaBody.setNetValue((float) 9.32);
        respostaCobrancaBody.setBillingType("CREDIT_CARD");
        respostaCobrancaBody.setConfirmedDate("2023-01-08");
        respostaCobrancaBody.setCreditCard(cartaoCredito);
        respostaCobrancaBody.setStatus("CONFIRMED");
        respostaCobrancaBody.setDueDate("2024-01-08");
        respostaCobrancaBody.setOriginalDueDate( "2024-01-08");
        respostaCobrancaBody.setClientPaymentDate("2023-01-08");
        respostaCobrancaBody.setInvoiceUrl( "https://sandbox.asaas.com/i/4563534755995298");
        respostaCobrancaBody.setInvoiceNumber("01719480");
        respostaCobrancaBody.setCreditDate("2023-02-09");
        respostaCobrancaBody.setEstimatedCreditDate("2023-02-09");
        respostaCobrancaBody.setTransactionReceiptUrl("https://sandbox.asaas.com/comprovantes/9341657795430967");

        ResponseEntity<Object> respostaCobranca = new ResponseEntity<>(respostaCobrancaBody, HttpStatus.OK);

        when(mockedRestTemplate.postForEntity(anyString(), any(), any())).thenReturn(respostaCobranca);
        when(mockedCartaoRepository.findByCiclista(novaCobranca.getCiclista())).thenReturn(dadosToken);
        when(mockedCobrancaRespository.save(any())).thenReturn(any());

        ResponseEntity<DadosCobranca> respostaRecebida = cobrancaService.realizarCobranca(novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());       
    }

    @Test
    void realizar_cobranca_not_found() {       
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 10.00 , "7b7476c7-60a7-46a3-b7fe-45d28eb18e99");
        DadosToken dadosToken = new DadosToken(novaCobranca.getCiclista(), "cus_000005077278", "4ae91611-5a92-42bf-ad17-a45124c11b19");

        APICartaoTokenResponse cartaoCredito = new APICartaoTokenResponse();
        cartaoCredito.setCreditCardBrand("MASTERCARD");
        cartaoCredito.setCreditCardNumber("8829");
        cartaoCredito.setCreditCardToken("c8594e1d-18bd-4520-af74-be2a45820b41");

        when(mockedCartaoRepository.findByCiclista(novaCobranca.getCiclista())).thenReturn(dadosToken);
        when(mockedRestTemplate.postForEntity(anyString(), any(), any())).thenThrow(ResourceNotFoundException.class);

        assertThrows(
            ResourceNotFoundException.class, 
            ()->{
                cobrancaService.realizarCobranca(novaCobranca);
            }
        );   
    }

    //Testes resgatar cobrança
    @Test
    void resgatar_cobranca_por_id() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        APICartaoTokenResponse cartaoCredito = new APICartaoTokenResponse();
        cartaoCredito.setCreditCardBrand("MASTERCARD");
        cartaoCredito.setCreditCardNumber("8829");
        cartaoCredito.setCreditCardToken("c8594e1d-18bd-4520-af74-be2a45820b41");

        AsaasCobrancaResponseDTO respostaCobrancaBody = new AsaasCobrancaResponseDTO();
        respostaCobrancaBody.setObject("payment");
        respostaCobrancaBody.setId("pay_4563534755995298");
        respostaCobrancaBody.setDateCreated("2023-01-08");
        respostaCobrancaBody.setCustomer("cus_000005075166");
        respostaCobrancaBody.setValue((float) 10.00);
        respostaCobrancaBody.setNetValue((float) 9.32);
        respostaCobrancaBody.setBillingType("CREDIT_CARD");
        respostaCobrancaBody.setConfirmedDate("2023-01-08");
        respostaCobrancaBody.setCreditCard(cartaoCredito);
        respostaCobrancaBody.setStatus("CONFIRMED");
        respostaCobrancaBody.setDueDate("2024-01-08");
        respostaCobrancaBody.setOriginalDueDate( "2024-01-08");
        respostaCobrancaBody.setClientPaymentDate("2023-01-08");
        respostaCobrancaBody.setInvoiceUrl( "https://sandbox.asaas.com/i/4563534755995298");
        respostaCobrancaBody.setInvoiceNumber("01719480");
        respostaCobrancaBody.setCreditDate("2023-02-09");
        respostaCobrancaBody.setEstimatedCreditDate("2023-02-09");
        respostaCobrancaBody.setTransactionReceiptUrl("https://sandbox.asaas.com/comprovantes/9341657795430967");

        ResponseEntity<AsaasCobrancaResponseDTO> respostaCobranca = new ResponseEntity<AsaasCobrancaResponseDTO>(respostaCobrancaBody, HttpStatus.OK);

        when(mockedRestTemplate.exchange(
            "https://sandbox.asaas.com/api/v3/payments/{id}", 
            HttpMethod.GET, 
            request, 
            AsaasCobrancaResponseDTO.class, 
            "pay_7712587216316033"))
            .thenReturn(respostaCobranca);

        ResponseEntity<AsaasCobrancaResponseDTO> respostaRecebida = cobrancaService.resgatarCobranca(headers, "pay_7712587216316033");

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    @Test
    void resgatar_cobranca_por_id_not_found() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        when(mockedRestTemplate.exchange(
            "https://sandbox.asaas.com/api/v3/payments/{id}", 
            HttpMethod.GET, 
            request, 
            AsaasCobrancaResponseDTO.class, 
            "pay_7712587216316033"))
            .thenThrow(ResourceNotFoundException.class);

        assertThrows(
            ResourceNotFoundException.class, 
            ()->{
                cobrancaService.resgatarCobranca(headers, "pay_7712587216316033");
            }
        );
    }


    //Teste colocar cobrança na fila
    @Test
    void colocar_cobranca_na_fila() {

        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 10.00, "7b7476c7-60a7-46a3-b7fe-45d28eb18e99");
        
        when(mockedCobrancaRespository.save(any())).thenReturn(any());

        ResponseEntity<DadosCobranca> resposta = cobrancaService.colocarCobrancaFila(novaCobranca);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
    
}
