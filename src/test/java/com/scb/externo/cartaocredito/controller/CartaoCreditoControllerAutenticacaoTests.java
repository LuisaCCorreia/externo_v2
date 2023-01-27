package com.scb.externo.cartaocredito.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import com.scb.externo.controller.cartaocredito.CartaoCreditoController;
import com.scb.externo.service.cartaocredito.CartaoCreditoService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CartaoCreditoControllerAutenticacaoTests {

    @Mock
    CartaoCreditoService mockedCartaoService;

    @InjectMocks
    CartaoCreditoController cartaoController;
  /*  
    @Test
    void autenticacao_cvv_Invalido() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("12345", "André", "5162306219378829", "2024-05-12");
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
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("1234", "André", "5162306219378829", "20205-12");
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
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("1234", "André", "a", "2024-05-12");
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
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("1234", null, "5162306219378829", "2024-05-12");
        String mensagemEsperada = "Dados Inválidos";
        String mensagemRecebida = "";

        try {
            cartaoController.autenticarCartao(novoCartao);
        } catch (ResourceInvalidException e) {
           mensagemRecebida = e.getMessage();
        }

        assertEquals(mensagemEsperada, mensagemRecebida);
        
    }
*/
  /*  @Test
    void autenticacao_valida() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("1234", "Victor", "5162306219378829", "2024-05-12");
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);

        APICartaoTokenResponse respostaService = new APICartaoTokenResponse();
        respostaService.setCreditCardNumber("5162306219378829");
        respostaService.setCreditCardBrand("MASTERCARD");
        respostaService.setCreditCardToken("4ae91611-5a92-42bf-ad17-a45124c11b19");
        when(mockedCartaoService.autenticarCartao(headers, novoCartao)).thenReturn(new ResponseEntity<JSONObject>(respostaService, HttpStatus.OK));

        ResponseEntity<JSONObject> respostaRecebida = cartaoController.autenticarCartao(headers, novoCartao);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }
*//*
    @Test
    void autenticacao_not_found_exception() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("1234", "Victor", "5162306219378829", "2024-05-12");
        when(mockedCartaoService.autenticarCartao(novoCartao)).thenThrow(ResourceNotFoundException.class);

       assertThrows(ResourceNotFoundException.class, 
       () -> {
         cartaoController.autenticarCartao(novoCartao);
       }); 
    }
*/
}
