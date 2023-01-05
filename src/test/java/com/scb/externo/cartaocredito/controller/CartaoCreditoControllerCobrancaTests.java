package com.scb.externo.cartaocredito.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.scb.externo.models.cartaocredito.CobrancaStatus;
import com.scb.externo.models.exceptions.ResourceInvalidException;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.service.cartaocredito.CartaoCreditoService;
import com.scb.externo.shared.NovaCobrancaDTO;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CartaoCreditoControllerCobrancaTests {

    @Mock
    CartaoCreditoService mockedCartaoService;

    @InjectMocks
    CartaoCreditoController cartaoController;

    @Test
    void cobranca_id_ciclista_Invalido() {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 5,"1234568");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        String mensagemEsperada = "Dados inválidos.";
        String mensagemRecebida = "";

        try {
            cartaoController.realizarCobranca(headers, novaCobranca);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }

        assertEquals(mensagemEsperada, mensagemRecebida);
    }

    // Na API da Asaas o valor mínimo para a cobrança é de 5 reais.
    @Test
    void cobranca_valor_invalido() {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 4.99,"7b7476c7-60a7-46a3-b7fe-45d28eb18e99");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        String mensagemEsperada = "Dados inválidos.";
        String mensagemRecebida = "";

        try {
            cartaoController.realizarCobranca(headers, novaCobranca);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }

        assertEquals(mensagemEsperada, mensagemRecebida);
    }

    @Test
    void cobranca_valida() {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 5,"7b7476c7-60a7-46a3-b7fe-45d28eb18e99");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);

        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(date);  

        
        DadosCobranca dadosRetornados = new DadosCobranca();

        dadosRetornados.setCiclista(novaCobranca.getCiclista());
        dadosRetornados.setHoraFinalizacao(strDate);
        dadosRetornados.setHoraSolicitacao(strDate);
        dadosRetornados.setId("pay_6676054201746363");
        dadosRetornados.setStatus(CobrancaStatus.PAGA.getStatus());
        dadosRetornados.setValor(novaCobranca.getValor());
        
        when(mockedCartaoService.realizarCobranca(headers, novaCobranca)).thenReturn(new ResponseEntity<DadosCobranca>(dadosRetornados, HttpStatus.OK));

        ResponseEntity<DadosCobranca> respostaRecebida = cartaoController.realizarCobranca(headers, novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }
    
}
