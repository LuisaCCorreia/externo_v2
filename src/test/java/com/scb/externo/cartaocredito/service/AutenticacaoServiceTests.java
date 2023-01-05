package com.scb.externo.cartaocredito.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.scb.externo.repository.cartaocredito.DadosCartaoRepository;
import com.scb.externo.service.cartaocredito.AutenticarDadosService;
import com.scb.externo.shared.APICartaoDeCreditoResponseBody;
import com.scb.externo.shared.APICartaoTokenResponse;
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
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("access_token", Key.ASAASKEY);
        NovoCartaoDTO novoCartao = new NovoCartaoDTO("214", "Maria", "5162306219378829", "2024-05-12");
        
        ResponseEntity<APICartaoTokenResponse> respostaRecebida = autenticacaoService.autenticarCartao(headers, novoCartao);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    //Esse método faz parte da autenticação de cartão porque a API exige que um cliente seja criado antes de o
    // cartão ser autenticado.
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
