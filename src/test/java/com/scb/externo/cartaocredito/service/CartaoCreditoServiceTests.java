package com.scb.externo.cartaocredito.service;

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
import com.scb.externo.models.cartaocredito.CobrancaStatus;
import com.scb.externo.models.exceptions.ResourceNotFoundException;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.service.cartaocredito.AutenticarDadosService;
import com.scb.externo.service.cartaocredito.CartaoCreditoService;
import com.scb.externo.service.cartaocredito.CobrancaService;
import com.scb.externo.shared.NovaCobrancaDTO;
import com.scb.externo.shared.NovoCartaoDTO;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CartaoCreditoServiceTests {

    @Mock
    CobrancaService mockedCobrancaService;

    @Mock
    AutenticarDadosService mockedAutenticacaoService;

    @InjectMocks
    CartaoCreditoService cartaoService;

    //Testes de autenticação
    /*
    @Test
    void autenticacao_valida() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("1234", "Victor", "5162306219378829", "2024-05-12");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);

        APICartaoTokenResponse respostaService = new APICartaoTokenResponse();
        respostaService.setCreditCardNumber("5162306219378829");
        respostaService.setCreditCardBrand("MASTERCARD");
        respostaService.setCreditCardToken("4ae91611-5a92-42bf-ad17-a45124c11b19");
        when(mockedAutenticacaoService.autenticarCartao(headers, novoCartao)).thenReturn(new ResponseEntity<APICartaoTokenResponse>(respostaService, HttpStatus.OK));

        ResponseEntity<APICartaoTokenResponse> respostaRecebida = cartaoService.autenticarCartao(headers, novoCartao);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }*/

    @Test
    void autenticacao_invalida_not_found() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("1234", "Victor", "5162306219378829", "2024-05-12");

        when(mockedAutenticacaoService.autenticarCartao(novoCartao)).thenThrow(ResourceNotFoundException.class);

        assertThrows(
            ResourceNotFoundException.class, 
            ()->{
                cartaoService.autenticarCartao(novoCartao);
            }
        );
    }

    //Testes de realizar cobrança
    @Test
    void realizar_cobranca_valida() throws JSONException, IOException, InterruptedException {
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
        
        when(mockedCobrancaService.realizarCobranca(novaCobranca)).thenReturn(new ResponseEntity<DadosCobranca>(dadosRetornados, HttpStatus.OK));

        ResponseEntity<DadosCobranca> respostaRecebida = cartaoService.realizarCobranca(novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }   

    @Test
    void realizar_cobranca_not_found() throws JSONException, IOException, InterruptedException {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 5,"7b7476c7-60a7-46a3-b7fe-45d28eb18e99");

        when(mockedCobrancaService.realizarCobranca(novaCobranca)).thenThrow(ResourceNotFoundException.class);

        assertThrows(
            ResourceNotFoundException.class,
            () -> {
                cartaoService.realizarCobranca(novaCobranca);
            }
        );
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

        ResponseEntity<AsaasCobrancaResponseDTO> cobranca = new ResponseEntity<AsaasCobrancaResponseDTO>(respostaCobrancaBody, HttpStatus.OK);
        
        when(mockedCobrancaService.resgatarCobranca( anyString())).thenReturn(cobranca);

        ResponseEntity<AsaasCobrancaResponseDTO> respostaRecebida = cartaoService.resgatarCobranca(headers, cobrancaId);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }*/

    @Test
    void resgatar_cobranca_not_found_exception() throws JSONException, IOException, InterruptedException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);

        when(mockedCobrancaService.resgatarCobranca( anyString())).thenThrow(ResourceNotFoundException.class);

       assertThrows(ResourceNotFoundException.class, 
       () -> {
         cartaoService.resgatarCobranca( "pay_7712587216316033");
       }); 
    }

    //Teste de colocar cobrança na fila
    @Test
    void colocar_cobranca_na_fila() {
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

        when(mockedCobrancaService.colocarCobrancaFila(any())).thenReturn(respostaMockedCartaoService);
        ResponseEntity<DadosCobranca> respostaRecebida = cartaoService.colocarCobrancaFila(novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }
    
}
