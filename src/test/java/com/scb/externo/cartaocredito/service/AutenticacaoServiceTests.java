package com.scb.externo.cartaocredito.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.scb.externo.consts.Key;
import com.scb.externo.models.mongodb.DadosToken;
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.service.cartaocredito.AutenticarDadosService;
import com.scb.externo.shared.APICartaoDeCreditoResponseBody;
import com.scb.externo.shared.APICartaoTokenResponse;
import com.scb.externo.shared.CartaoCreditoAsaas;
import com.scb.externo.shared.NovaTokenizacao;
import com.scb.externo.shared.NovoCartaoDTO;
import com.scb.externo.shared.NovoCliente;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AutenticacaoServiceTests {

    @Mock
    RestTemplate mockedRestTemplate;

    @Mock
    DadosCartaoRepository mockedDadosCartaoRepository;

    @InjectMocks
    AutenticarDadosService autenticacaoService;


    @Test
    void autenticar_cartao() {
        String autenticarCartaoURL = "https://sandbox.asaas.com/api/v3/creditCard/tokenize";
        String criarClienteURL = "https://sandbox.asaas.com/api/v3/customers";
        
        // Parâmetros do autenticacaoService
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("214", "Maria", "5162306219378829", "2024-05-12");

        // A API da Asaas precisa criar o cliente antes de fazer a autenticação de cartão
        //Requisição para API de criar cliente
        NovoCliente novoCliente = new NovoCliente();
        novoCliente.setName(novoCartao.getNomeTitular());

        //Resposta da API de criar cliente
        APICartaoDeCreditoResponseBody clienteCriado = new APICartaoDeCreditoResponseBody();
        clienteCriado.setObject("customer");
        clienteCriado.setId("cus_000005078171");
        clienteCriado.setDateCreated("2023-01-03");
        clienteCriado.setName(novoCartao.getNomeTitular());
        clienteCriado.setCountry("Brasil");
        clienteCriado.setCanDelete(true);
        clienteCriado.setCanEdit(true);

        //
        LocalDate data = LocalDate.parse(novoCartao.getValidade());
        CartaoCreditoAsaas cartaoAsaas = new CartaoCreditoAsaas(novoCartao.getNomeTitular(), novoCartao.getNumero(), 
        Integer.toString(data.getMonthValue()), Integer.toString(data.getYear()), novoCartao.getCvv());
        NovaTokenizacao novaTokenizacao = new NovaTokenizacao(clienteCriado.getId(), cartaoAsaas);


        // Resposta API que gera o Token do cartão
        APICartaoTokenResponse respostaToken = new APICartaoTokenResponse();
        respostaToken.setCreditCardToken("6870ab11-6970-459d-8054-3a899a144470");
        respostaToken.setCreditCardBrand("MASTERCARD");
        respostaToken.setCreditCardNumber("8829");

        //Dados a serem armazenados no banco de dados
        DadosToken dadosToken = new DadosToken(UUID.randomUUID().toString(),clienteCriado.getId(), respostaToken.getCreditCardToken());
        
        when(mockedRestTemplate.postForEntity(criarClienteURL, new HttpEntity<>(novoCliente, headers), 
        APICartaoDeCreditoResponseBody.class))
        .thenReturn(new ResponseEntity<APICartaoDeCreditoResponseBody>(clienteCriado, HttpStatus.OK));
        when(mockedRestTemplate.postForEntity(autenticarCartaoURL, new HttpEntity<>(novaTokenizacao, headers), APICartaoTokenResponse.class))
        .thenReturn(new ResponseEntity<APICartaoTokenResponse>(respostaToken, HttpStatus.OK));

        verify(mockedDadosCartaoRepository, times(0)).save(dadosToken);
        
        ResponseEntity<APICartaoTokenResponse> respostaRecebida = autenticacaoService.autenticarCartao(headers, novoCartao);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    @Test
    void criar_cliente() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        NovoCliente novoCliente = new NovoCliente();
        novoCliente.setName("Maria");

        ResponseEntity<APICartaoDeCreditoResponseBody> respostaRecebida = autenticacaoService.criarCliente(headers, novoCliente.getName());

        assertEquals("Maria", respostaRecebida.getBody().getName());
    }
    
}
