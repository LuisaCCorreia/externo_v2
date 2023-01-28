package com.scb.externo.cartaocredito.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.scb.externo.service.cartaocredito.AutenticarDadosService;
import com.scb.externo.shared.NovoCartaoDTO;
import com.scb.externo.shared.NovoCliente;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTests {

    @Autowired
    AutenticarDadosService autenticacaoService;

    @Test
    void autenticar_cartao_valido() throws IOException, InterruptedException, JSONException {        
        NovoCartaoDTO novoCartao = new NovoCartaoDTO();
        novoCartao.setId(50);
        novoCartao.setCvv("214");
        novoCartao.setNomeTitular("Maria");
        novoCartao.setNumero("5162306219378829");
        novoCartao.setValidade("2024-05-12");
        
        ResponseEntity<String> respostaRecebida = autenticacaoService.autenticarCartao(novoCartao);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    /*Esse método faz parte da autenticação de cartão porque a API exige que um cliente seja criado antes de o
     cartão ser autenticado. */
    @Test
    void criar_cliente_valido() throws IOException, InterruptedException, JSONException {
        NovoCliente novoCliente = new NovoCliente();
        novoCliente.setName("Maria");


        ResponseEntity<JSONObject> respostaRecebida = autenticacaoService.criarCliente(novoCliente.getName());

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }
    
}
