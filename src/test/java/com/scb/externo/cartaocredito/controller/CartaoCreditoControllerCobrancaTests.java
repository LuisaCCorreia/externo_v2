package com.scb.externo.cartaocredito.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.scb.externo.models.cartaocredito.CobrancaStatus;
import com.scb.externo.models.exceptions.ResourceInvalidException;
import com.scb.externo.models.exceptions.ResourceNotFoundException;
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

    //Testes de realizar cobrança
    // Na API da Asaas o valor mínimo para a cobrança é de 5 reais.
   @Test    
    void cobranca_valor_invalido() throws JSONException, IOException, InterruptedException {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 4.99,1);
        String mensagemEsperada = "Dados Inválidos";
        String mensagemRecebida = "";

        try {
            cartaoController.realizarCobranca(novaCobranca);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }

        assertEquals(mensagemEsperada, mensagemRecebida);
    }

    @Test
    void cobranca_valida() throws JSONException, IOException, InterruptedException {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 5,1);
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
        
        when(mockedCartaoService.realizarCobranca(novaCobranca)).thenReturn(new ResponseEntity<DadosCobranca>(dadosRetornados, HttpStatus.OK));

        ResponseEntity<DadosCobranca> respostaRecebida = cartaoController.realizarCobranca(novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    @Test
    void cobranca_not_found_exception() throws JSONException, IOException, InterruptedException {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 5,1);

        when(mockedCartaoService.realizarCobranca(any())).thenThrow(ResourceNotFoundException.class);

       assertThrows(ResourceNotFoundException.class, 
       () -> {
         cartaoController.realizarCobranca(novaCobranca);
       }); 
    }

    //Testes de resgatar cobrança
    @Test
    void resgatar_cobranca_por_id_sucesso() throws JSONException, IOException, InterruptedException {
        String cobrancaId = "pay_7712587216316033";        

        String respostaCobrancaBody = "{\"object\":\"payment\", \"id\":\"pay_4563534755995298\", \"dateCreated\":\"2023-01-08\", "+
        "\"customer\":\"cus_000005075166\", \"value\":10.00, \"netValue\":9.32, \"billingType\":\"CREDIT_CARD\", \"confirmedDate\":"+
        "\"2023-01-08\", \"creditCard\":{\"creditCardBrand\":\"MASTERCARD\", \"creditCardNumber\":\"8829\", \"creditCardToken\":"+
        "\"c8594e1d-18bd-4520-af74-be2a45820b41\"},\"status\":\"CONFIRMED\", \"dueDate\":\"2024-01-08\", \"originalDueDate\":"+
        "\"2024-01-08\", \"clientPaymentDate\":\"2023-01-08\", \"invoiceUrl\":\"https://sandbox.asaas.com/i/4563534755995298\", "+
        "\"invoiceNumber\":\"01719480\", \"creditDate\":\"2023-02-09\", \"estimatedCreditDate\":\"2023-02-09\", \"transactionReceiptUrl\":"+
        "\"https://sandbox.asaas.com/comprovantes/9341657795430967\"}";


        ResponseEntity<String> cobranca = new ResponseEntity<>(respostaCobrancaBody, HttpStatus.OK);
        
        when(mockedCartaoService.resgatarCobranca(anyString())).thenReturn(cobranca);

        ResponseEntity<String> respostaRecebida = cartaoController.resgatarCobranca(cobrancaId);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    @Test
    void resgatar_cobranca_not_found_exception() throws JSONException, IOException, InterruptedException {

        when(mockedCartaoService.resgatarCobranca( anyString())).thenThrow(ResourceNotFoundException.class);

       assertThrows(ResourceNotFoundException.class, 
       () -> {
         cartaoController.resgatarCobranca( "pay_7712587216316033");
       }); 
    }

    // Testes de colocar cobrança na fila
    @Test
    void colocar_cobranca_na_fila_sucesso() {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 5,1);

        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        String strDate = dateFormat.format(date); 
        
        DadosCobranca dadosRetornados = new DadosCobranca();

        dadosRetornados.setCiclista(novaCobranca.getCiclista());
        dadosRetornados.setHoraFinalizacao(strDate);
        dadosRetornados.setHoraSolicitacao(strDate);
        dadosRetornados.setId("pay_6676054201746363");
        dadosRetornados.setStatus(CobrancaStatus.PENDENTE.getStatus());
        dadosRetornados.setValor(novaCobranca.getValor());

        ResponseEntity<DadosCobranca> respostaMockedCartaoService = new ResponseEntity<>(dadosRetornados, HttpStatus.OK);

        when(mockedCartaoService.colocarCobrancaFila(any())).thenReturn(respostaMockedCartaoService);
        ResponseEntity<DadosCobranca> respostaRecebida = cartaoController.colocarCobrancaFila(novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }
  
    @Test
    void colocar_cobranca_na_fila_id_ciclista_Invalido() {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 5, 0);
        String mensagemEsperada = "Dados Inválidos";
        String mensagemRecebida = "";

        try {
            cartaoController.colocarCobrancaFila(novaCobranca);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }

        assertEquals(mensagemEsperada, mensagemRecebida);
    }

    // Na API da Asaas o valor mínimo para a cobrança é de 5 reais.
  
    @Test
    void colocar_cobranca_na_fila_valor_invalido() {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 4.99,1);
        String mensagemEsperada = "Dados Inválidos";
        String mensagemRecebida = "";

        try {
            cartaoController.colocarCobrancaFila(novaCobranca);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }

        assertEquals(mensagemEsperada, mensagemRecebida);
    }

   
}
