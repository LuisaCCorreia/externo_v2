package com.scb.externo.cartaocredito.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.scb.externo.consts.Key;
import com.scb.externo.controller.cartaocredito.CartaoCreditoController;
import com.scb.externo.models.exceptions.ResourceInvalidException;
import com.scb.externo.service.cartaocredito.CartaoCreditoService;
import com.scb.externo.shared.APICartaoTokenResponse;
import com.scb.externo.shared.NovoCartaoDTO;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CartaoCreditoControllerAutenticacaoTests {

    @Mock
    CartaoCreditoService mockedCartaoService;

    @InjectMocks
    CartaoCreditoController cartaoController;
    
    @Test
    void autenticacao_cvv_Invalido() {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("12345", "André", "5162306219378829", "2024-05-12");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        String mensagemEsperada = "Dados inválidos.";
        String mensagemRecebida = "";

        try {
            cartaoController.autenticarCartao(headers, novoCartao);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }
        assertEquals(mensagemEsperada, mensagemRecebida); 
    }

    @Test
    void autenticacao_Data_Invalida() {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("1234", "André", "5162306219378829", "20205-12");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        String mensagemEsperada = "Dados inválidos.";
        String mensagemRecebida = "";

        try {
            cartaoController.autenticarCartao(headers, novoCartao);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }
        assertEquals(mensagemEsperada, mensagemRecebida);    
    }

    @Test
    void autenticacao_Numero_Invalido() {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("1234", "André", "a", "2024-05-12");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        String mensagemEsperada = "Dados inválidos.";
        String mensagemRecebida = "";

        try {
            cartaoController.autenticarCartao(headers, novoCartao);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }

        assertEquals(mensagemEsperada, mensagemRecebida);   
    }

    @Test
    void autenticacao_Nome_Invalido() {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("1234", null, "5162306219378829", "2024-05-12");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        String mensagemEsperada = "Dados inválidos.";
        String mensagemRecebida = "";

        try {
            cartaoController.autenticarCartao(headers, novoCartao);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }

        assertEquals(mensagemEsperada, mensagemRecebida);
        
    }

    @Test
    void autenticacao_Valida() {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("1234", "Victor", "5162306219378829", "2024-05-12");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);

        APICartaoTokenResponse respostaService = new APICartaoTokenResponse();
        respostaService.setCreditCardNumber("5162306219378829");
        respostaService.setCreditCardBrand("MASTERCARD");
        respostaService.setCreditCardToken("4ae91611-5a92-42bf-ad17-a45124c11b19");
        when(mockedCartaoService.autenticarCartao(headers, novoCartao)).thenReturn(new ResponseEntity<APICartaoTokenResponse>(respostaService, HttpStatus.OK));

        ResponseEntity<APICartaoTokenResponse> respostaRecebida = cartaoController.autenticarCartao(headers, novoCartao);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }
}
