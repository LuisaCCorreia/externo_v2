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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.scb.externo.consts.Key;
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
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 4.99,"7b7476c7-60a7-46a3-b7fe-45d28eb18e99");
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
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 5,"7b7476c7-60a7-46a3-b7fe-45d28eb18e99");
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
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 5,"7b7476c7-60a7-46a3-b7fe-45d28eb18e99");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);

        when(mockedCartaoService.realizarCobranca(any())).thenThrow(ResourceNotFoundException.class);

       assertThrows(ResourceNotFoundException.class, 
       () -> {
         cartaoController.realizarCobranca(novaCobranca);
       }); 
    }

    //Testes de resgatar cobrança
    /*
    @Test
    void resgatar_cobranca_por_id_sucesso() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);

        String cobrancaId = "pay_7712587216316033";

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

        ResponseEntity<String> cobranca = new ResponseEntity<AsaasCobrancaResponseDTO>(respostaCobrancaBody, HttpStatus.OK);
        
        when(mockedCartaoService.resgatarCobranca(anyString())).thenReturn(cobranca);

        ResponseEntity<String> respostaRecebida = cartaoController.resgatarCobranca(cobrancaId);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }*/

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
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 5,"7b7476c7-60a7-46a3-b7fe-45d28eb18e99");

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
  /*
    @Test
    void colocar_cobranca_na_fila_id_ciclista_Invalido() {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 5,"1234568");
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
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 4.99,"7b7476c7-60a7-46a3-b7fe-45d28eb18e99");
        String mensagemEsperada = "Dados Inválidos";
        String mensagemRecebida = "";

        try {
            cartaoController.colocarCobrancaFila(novaCobranca);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }

        assertEquals(mensagemEsperada, mensagemRecebida);
    }
*/
   
}
