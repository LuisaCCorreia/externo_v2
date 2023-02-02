package com.scb.externo.cartaocredito.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.scb.externo.models.mongodb.DadosCobranca;
import com.scb.externo.service.cartaocredito.CartaoCreditoService;
import com.scb.externo.shared.NovaCobrancaDTO;
import com.scb.externo.shared.NovoCartaoDTO;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CartaoCreditoServiceIntegracaoTests {

    @Autowired
    CartaoCreditoService cartaoService;

    //Testes de autenticação
    @Test
    void autenticacao_valida() throws IOException, InterruptedException, JSONException {
        NovoCartaoDTO novoCartao = new NovoCartaoDTO();
        novoCartao.setId(1);
        novoCartao.setCvv("1234");
        novoCartao.setNomeTitular("Victor");
        novoCartao.setNumero("5162306219378829");
        novoCartao.setValidade("2024-05-12");

        ResponseEntity<String> respostaRecebida = cartaoService.autenticarCartao( novoCartao);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    //Testes de realizar cobrança
    @Test
    void realizar_cobranca_valida() throws JSONException, IOException, InterruptedException {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO();
        novaCobranca.setCiclista(1);
        novaCobranca.setValor(5);
        
        ResponseEntity<DadosCobranca> respostaRecebida = cartaoService.realizarCobranca(novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }    

    //Testes de resgatar cobrança
    @Test
    void resgatar_cobranca_por_id_sucesso() throws JSONException, IOException, InterruptedException {
        String cobrancaId = "pay_7712587216316033";

        ResponseEntity<String> respostaRecebida = cartaoService.resgatarCobranca( cobrancaId);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }

    @Test
    void colocar_cobranca_na_fila() {
        NovaCobrancaDTO novaCobranca = new NovaCobrancaDTO();
        novaCobranca.setCiclista(1);
        novaCobranca.setValor(5);

        ResponseEntity<DadosCobranca> respostaRecebida = cartaoService.colocarCobrancaFila(novaCobranca);

        assertEquals(HttpStatus.OK, respostaRecebida.getStatusCode());
    }
}
