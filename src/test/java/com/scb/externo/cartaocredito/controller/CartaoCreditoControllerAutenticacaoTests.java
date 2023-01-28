package com.scb.externo.cartaocredito.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.scb.externo.controller.cartaocredito.CartaoCreditoController;
import com.scb.externo.models.exceptions.ResourceInvalidException;
import com.scb.externo.models.exceptions.ResourceNotFoundException;
import com.scb.externo.service.cartaocredito.CartaoCreditoService;
import com.scb.externo.shared.NovoCartaoDTO;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CartaoCreditoControllerAutenticacaoTests {

    @Mock
    CartaoCreditoService mockedCartaoService;

    @InjectMocks
    CartaoCreditoController cartaoController;
   
    @Test
    void autenticacao_cvv_Invalido() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO();
        novoCartao.setId(1);
        novoCartao.setCvv("12345");
        novoCartao.setNomeTitular("André");
        novoCartao.setNumero("5162306219378829");
        novoCartao.setValidade("2024-05-12");

        String mensagemEsperada = "Dados Inválidos";
        String mensagemRecebida = "";

        try {
            cartaoController.autenticarCartao(novoCartao);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }
        assertEquals(mensagemEsperada, mensagemRecebida); 
    }

    @Test
    void autenticacao_Data_Invalida() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO();
        novoCartao.setId(1);
        novoCartao.setCvv("1234");
        novoCartao.setNomeTitular("André");
        novoCartao.setNumero("5162306219378829");
        novoCartao.setValidade("20205-12");

        String mensagemEsperada = "Dados Inválidos";
        String mensagemRecebida = "";

        try {
            cartaoController.autenticarCartao(novoCartao);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }
        assertEquals(mensagemEsperada, mensagemRecebida);    
    }

    @Test
    void autenticacao_Numero_Invalido() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO();
        novoCartao.setId(1);
        novoCartao.setCvv("1234");
        novoCartao.setNomeTitular("André");
        novoCartao.setNumero("a");
        novoCartao.setValidade("2024-05-12");

        String mensagemEsperada = "Dados Inválidos";
        String mensagemRecebida = "";

        try {
            cartaoController.autenticarCartao(novoCartao);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }

        assertEquals(mensagemEsperada, mensagemRecebida);   
    }

    @Test
    void autenticacao_Nome_Invalido() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO();
        novoCartao.setId(1);
        novoCartao.setCvv("1234");
        novoCartao.setNomeTitular(null);
        novoCartao.setNumero("5162306219378829");
        novoCartao.setValidade("2024-05-12");

        String mensagemEsperada = "Dados Inválidos";
        String mensagemRecebida = "";

        try {
            cartaoController.autenticarCartao(novoCartao);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }

        assertEquals(mensagemEsperada, mensagemRecebida);
        
    }

    @Test
    void autenticacao_valida() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO();
        novoCartao.setId(1);
        novoCartao.setCvv("1234");
        novoCartao.setNomeTitular("Victor");
        novoCartao.setNumero("5162306219378829");
        novoCartao.setValidade("2024-05-12");


        String respostaService = "{\"creditCardNumber\":\"5162306219378829\", \"creditCardBrand\":\"MASTERCARD\", \"creditCardToken\":"+
        "\"4ae91611-5a92-42bf-ad17-a45124c11b19\"}";

        when(mockedCartaoService.autenticarCartao( novoCartao)).thenReturn(new ResponseEntity<>(respostaService, HttpStatus.OK));

        ResponseEntity<String> respostaRecebida = cartaoController.autenticarCartao(novoCartao);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    @Test
    void autenticacao_not_found_exception() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO();
        novoCartao.setId(1);
        novoCartao.setCvv("1234");
        novoCartao.setNomeTitular("Victor");
        novoCartao.setNumero("5162306219378829");
        novoCartao.setValidade("2024-05-12");
        
        when(mockedCartaoService.autenticarCartao(novoCartao)).thenThrow(ResourceNotFoundException.class);

       assertThrows(ResourceNotFoundException.class, 
       () -> {
         cartaoController.autenticarCartao(novoCartao);
       }); 
    }

}
