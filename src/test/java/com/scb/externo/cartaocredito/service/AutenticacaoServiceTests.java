package com.scb.externo.cartaocredito.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.web.client.RestTemplate;
import com.scb.externo.consts.Key;
import com.scb.externo.models.exceptions.ResourceNotFoundException;
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.service.cartaocredito.AutenticarDadosService;
import com.scb.externo.shared.APICartaoDeCreditoResponseBody;
import com.scb.externo.shared.APICartaoTokenResponse;
import com.scb.externo.shared.NovoCartaoDTO;
import com.scb.externo.shared.NovoCliente;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTests {

    @Mock
    RestTemplate mockedRestTemplate;

    @Mock
    DadosCartaoRepository mockedDadosCartaoRepository;

    @InjectMocks
    AutenticarDadosService autenticacaoService;

    @Test
    void autenticar_cartao_valido() {        
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("214", "Maria", "5162306219378829", "2024-05-12");


        APICartaoDeCreditoResponseBody criarClienteBody = new APICartaoDeCreditoResponseBody();
        criarClienteBody.setObject("customer");
        criarClienteBody.setId("cus_000005080990");
        criarClienteBody.setDateCreated("2023-01-07");
        criarClienteBody.setName("Maria");
        criarClienteBody.setCanDelete(true);
        criarClienteBody.setCanEdit(true);
        criarClienteBody.setCountry("Brasil");

        APICartaoTokenResponse tokenResponse = new APICartaoTokenResponse();
        tokenResponse.setCreditCardBrand("MASTERCARD");
        tokenResponse.setCreditCardNumber("8829");
        tokenResponse.setCreditCardToken("2024-05-12");

        ResponseEntity<Object> respostaCriarCliente = new ResponseEntity<Object>(criarClienteBody, HttpStatus.OK);

        ResponseEntity<Object> respostaAutenticacao = new ResponseEntity<>(tokenResponse, HttpStatus.OK);

        when(mockedRestTemplate.postForEntity(anyString(), any(), any())).thenReturn(respostaCriarCliente).thenReturn(respostaAutenticacao);
        
        ResponseEntity<APICartaoTokenResponse> respostaRecebida = autenticacaoService.autenticarCartao(headers, novoCartao);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    @Test
    void autenticar_cartao_not_found_exception() {        
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("214", "Maria", "5162306219378829", "2024-05-12");

        when(mockedRestTemplate.postForEntity(anyString(), any(), any())).thenThrow(ResourceNotFoundException.class);
        
        assertThrows(
            ResourceNotFoundException.class, 
            ()->{
                autenticacaoService.autenticarCartao(headers, novoCartao);
            }
        );    
    }

    //Esse método faz parte da autenticação de cartão porque a API exige que um cliente seja criado antes de o
    // cartão ser autenticado.
    @Test
    void criar_cliente_valido() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        NovoCliente novoCliente = new NovoCliente();
        novoCliente.setName("Maria");

        APICartaoDeCreditoResponseBody resposta = new APICartaoDeCreditoResponseBody();
        resposta.setObject("customer");
        resposta.setId("cus_000005080990");
        resposta.setDateCreated("2023-01-07");
        resposta.setName(novoCliente.getName());
        resposta.setCanDelete(true);
        resposta.setCanEdit(true);
        resposta.setCountry("Brasil");

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(resposta, HttpStatus.OK);

        when(mockedRestTemplate.postForEntity(anyString(), any() , any())).thenReturn(responseEntity);

        ResponseEntity<APICartaoDeCreditoResponseBody> respostaRecebida = autenticacaoService.criarCliente(headers, novoCliente.getName());

        assertEquals("cus_000005080990", respostaRecebida.getBody().getId());
    }
    
}
