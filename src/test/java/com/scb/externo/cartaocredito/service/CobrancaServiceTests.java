package com.scb.externo.cartaocredito.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import com.scb.externo.repository.cartaocredito.CobrancaRepository;
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.service.cartaocredito.CobrancaService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CobrancaServiceTests {

    @Mock
    DadosCartaoRepository mockedCartaoRepository;

    @Mock
    CobrancaRepository mockedCobrancaRespository;

    @InjectMocks
    CobrancaService cobrancaService;

    //Testes realizar cobrança
   /* 
    @Test
    void realizar_cobranca_valida() throws JSONException, IOException, InterruptedException {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 10.00 , "30d3dcb7-0eba-41ea-9591-939f2b8df437");
        DadosToken dadosToken = new DadosToken(()1,novaCobranca.getCiclista(), "cus_000005095826", "20799520-7bc1-46f4-a679-6a17fab1c602");

       String respostaCobrancaBody = "{\"object\":\"payment\", \"id\":\"pay_4563534755995298\", \"dateCreated\":\"2023-01-26\","+
        "\"customer\":\"cus_000005075166\", \"value\": 10.00, \"netValue\":9.32, \"billingType\":\"CREDIT_CARD\", \"confirmedDate\":"
        +"\"2023-01-26\", \"creditCard\":{\"creditCardBrand\":\"MASTERCARD\", \"creditCardNumber\":\"8829\", \"creditCardToken\": "
        +"\"c8594e1d-18bd-4520-af74-be2a45820b41\"}, \"status\":\"CONFIRMED\", \"dueDate\":\"2024-01-08\", \"originalDueDate\":"+
        "\"2024-01-08\", \"clientPaymentDate\":\"2023-01-08\", \"invoiceUrl\":\"https://sandbox.asaas.com/i/4563534755995298\","+
        "\"invoiceNumber\":\"01719480\", \"creditDate\":\"2023-02-09\", \"estimatedCreditDate\":\"2023-02-09\", \"transactionReceiptUrl\""+
        ":\"https://sandbox.asaas.com/comprovantes/9341657795430967\"}";


        when(mockedCartaoRepository.findByCiclista(novaCobranca.getCiclista())).thenReturn(dadosToken);
        when(mockedCobrancaRespository.save(any())).thenReturn(any());

        ResponseEntity<DadosCobranca> respostaRecebida = cobrancaService.realizarCobranca(novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());       
    }*/

    /*@Test
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
    }*/
/* */
    //Testes resgatar cobrança
    /*
    @Test
    void resgatar_cobranca_por_id() throws JSONException, IOException, InterruptedException {
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

        ResponseEntity<String> respostaRecebida = cobrancaService.resgatarCobranca( "pay_7712587216316033");

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }*/
/* 
    @Test
    void resgatar_cobranca_por_id_not_found() {


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
                cobrancaService.resgatarCobranca("pay_7712587216316033");
            }
        );
    }*/


    //Teste colocar cobrança na fila
    /*
    @Test
    void colocar_cobranca_na_fila() {

        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO((float) 10.00, "7b7476c7-60a7-46a3-b7fe-45d28eb18e99");
        
        when(mockedCobrancaRespository.save(any())).thenReturn(any());

        ResponseEntity<DadosCobranca> resposta = cobrancaService.colocarCobrancaFila(novaCobranca);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }*/
    
}
