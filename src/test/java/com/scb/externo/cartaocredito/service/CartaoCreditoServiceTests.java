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
    @Test
    void autenticacao_valida() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO(1, "1234", "Victor", "5162306219378829", "2024-05-12");

        String respostaService = "{\"creditCardBrand\":\"MASTERCARD\", \"creditCardNumber\":\"8829\", \"creditCardToken\":"+
        "\"c8594e1d-18bd-4520-af74-be2a45820b41\"}";
        when(mockedAutenticacaoService.autenticarCartao(novoCartao)).thenReturn(new ResponseEntity<>(respostaService, HttpStatus.OK));

        ResponseEntity<String> respostaRecebida = cartaoService.autenticarCartao( novoCartao);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    @Test
    void autenticacao_invalida_not_found() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO(1,"1234", "Victor", "5162306219378829", "2024-05-12");

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
        
        when(mockedCobrancaService.realizarCobranca(novaCobranca)).thenReturn(new ResponseEntity<DadosCobranca>(dadosRetornados, HttpStatus.OK));

        ResponseEntity<DadosCobranca> respostaRecebida = cartaoService.realizarCobranca(novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }   

    @Test
    void realizar_cobranca_not_found() throws JSONException, IOException, InterruptedException {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 5,1);

        when(mockedCobrancaService.realizarCobranca(novaCobranca)).thenThrow(ResourceNotFoundException.class);

        assertThrows(
            ResourceNotFoundException.class,
            () -> {
                cartaoService.realizarCobranca(novaCobranca);
            }
        );
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
        
        when(mockedCobrancaService.resgatarCobranca( anyString())).thenReturn(cobranca);

        ResponseEntity<String> respostaRecebida = cartaoService.resgatarCobranca( cobrancaId);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    @Test
    void resgatar_cobranca_not_found_exception() throws JSONException, IOException, InterruptedException {
        when(mockedCobrancaService.resgatarCobranca( anyString())).thenThrow(ResourceNotFoundException.class);

       assertThrows(ResourceNotFoundException.class, 
       () -> {
         cartaoService.resgatarCobranca( "pay_7712587216316033");
       }); 
    }

    //Teste de colocar cobrança na fila
    @Test
    void colocar_cobranca_na_fila() {
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

        when(mockedCobrancaService.colocarCobrancaFila(any())).thenReturn(respostaMockedCartaoService);
        ResponseEntity<DadosCobranca> respostaRecebida = cartaoService.colocarCobrancaFila(novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }
}
